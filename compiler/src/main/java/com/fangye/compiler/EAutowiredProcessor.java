package com.fangye.compiler;

import com.fangye.annotation.EAutowired;
import com.fangye.annotation.enums.RouterType;
import com.fangye.compiler.factory.EAutowiredFactory;
import com.fangye.compiler.utils.Utils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({Config.ANNOTATION_EAUTOWIRED_TYPE})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class EAutowiredProcessor extends AbstractProcessor {

    private Elements elementUtils; // 类信息
    private Types typeUtils; // 具体类型
    private Messager messager; // 日志
    private Filer filer; // 生成文件

    // 临时map存储，用来存放被@EAutowired注解的属性集合，生成类文件时遍历
    // key:类节点, value:被@EAutowired注解的属性集合
    private Map<TypeElement, List<Element>> tempParameterMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 扫描的时候，看那些地方使用到了@EAutowired 注解
        if (!Utils.isEmpty(set)) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(EAutowired.class);

            // 有地方使用到了  @EAutowired
            if (!Utils.isEmpty(elements)) {

                for (Element element : elements) {
                    // 注解在属性的上面，属性节点父节点 是 类节点
                    TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

                    // 如果Map集合中的key：类节点存在，直接添加属性
                    if (tempParameterMap.containsKey(enclosingElement)) {
                        tempParameterMap.get(enclosingElement).add(element);
                    } else {
                        // 没有存放 key == StudyMainActivity
                        List<Element> fields = new ArrayList<>();
                        fields.add(element);
                        tempParameterMap.put(enclosingElement, fields);
                    }
                } // end for

                // 判断是否有需要生成的类文件
                if (Utils.isEmpty(tempParameterMap)) return true;

                // 通过Element工具类，获取ISyringe类型
                TypeElement activityType = elementUtils.getTypeElement(RouterType.ACTIVITY.getClassName());
                TypeElement parameterType = elementUtils.getTypeElement(Config.EROUTER_API_PARAMETER);


                try {
                    createAutowiredFile(activityType, parameterType);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return false;
    }

    /**
     * 生成文件
     *
     * @param activityType
     * @param parameterType
     * @throws IOException IOException
     *                     <p>
     *                     模板代码：
     *                     //public class StudyMainActivity$$Autowired implements IAutowired {
     *                     //    @Override
     *                     //    public void inject(Object target) {
     *                     //        StudyMainActivity t = (StudyMainActivity) target;
     *                     //        t.name = t.getIntent().getStringExtra("name");
     *                     //    }
     *                     //}
     */
    private void createAutowiredFile(TypeElement activityType, TypeElement parameterType) throws IOException {
        // Object target
        ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.OBJECT, Config.EROUTER_AUTOWIRED_PARAMETER_VAR).build();

        // 可能很多地方都使用了 @EAutowired 注解，那么就需要去遍历 仓库
        for (Map.Entry<TypeElement, List<Element>> entry : tempParameterMap.entrySet()) {
            // key == StudyMainActivity
            // value = [name,age]

            /**
             * 一、写方法，添加参数  parameterSpec
             */
            // Map集合中的key是类名，如：StudyMainActivity
            TypeElement typeElement = entry.getKey();

            // 如果类名的类型和Activity类型不匹配
            if (!typeUtils.isSubtype(typeElement.asType(), activityType.asType())) {
                throw new RuntimeException("@EAutowired 注解目前仅限用于Activity类之上");
            }

            // 获取类名 == StudyMainActivity
            ClassName className = ClassName.get(typeElement);

            // 方法架子搭起来了
            EAutowiredFactory factory = new EAutowiredFactory.Builder(parameterSpec)
                    .setMessager(messager)
                    .setClassName(className)
                    .build();

            // 只有一行
            // 添加方法的第一行
            factory.addFirstStatement();

            // 多行 循环
            // 遍历类里面的属性：然后就可以给方法增加内容
            for (Element fieldElement : entry.getValue()) {
                factory.buildStatement(fieldElement);
            }


            /**
             * 三、写类，并添加 通过factory 生成的方法
             */
            // 最终生成的类文件名（类名$$Parameter）    Order_MainActivity$$Parameter
            String finalClassName = typeElement.getSimpleName() + Config.EROUTER_AUTOWIRED_FILE_NAME;
            messager.printMessage(Diagnostic.Kind.NOTE, "APT生成获取参数类文件：" +
                    className.packageName() + "." + finalClassName);

            TypeSpec typeSpec = TypeSpec.classBuilder(finalClassName) // 类名
                    .addJavadoc(Config.EROUTER_AUTOWIRED_WARNING_TIPS)   //添加注释
                     .addSuperinterface(ClassName.get(parameterType)) // 实现ParameterLoad接口
                    .addModifiers(Modifier.PUBLIC) // public修饰符
                    .addMethod(factory.build()) // 方法的构建（方法参数 + 方法体）
                    .build();

            /**
             * 写包，添加上一步写的类，并创建文件
             */
            JavaFile.builder(className.packageName(), // 包名
                    typeSpec) // 类构建完成
                    .build() // JavaFile构建完成
                    .writeTo(filer); // 文件生成器开始生成类文件

        }
    }
}
