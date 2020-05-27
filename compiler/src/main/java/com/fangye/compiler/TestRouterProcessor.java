package com.fangye.compiler;

import com.fangye.annotation.TestRouter;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;

/**
 * 编译期会执行这个类
 */
// 编译期绑定注解，AutoService 会自动在META-INF/service文件下生成Processor的配置文件，
// 该文件中就是实现该服务接口的具体实现类，当外部程序使用这个模块时，就能通过META-INF/service里的配置文件找到具体的实现类
// 并加载实例化，完成模块的注入
@AutoService(Processor.class)
//监控某个注解，如果只有一个就不用写“{}”，具体的注解类放到Config中，方便管理
@SupportedAnnotationTypes({Config.ANNOTATION_TYPE1, Config.ANNOTATION_TYPE2})
// 使用的源代码版本 ，这个必须要写
@SupportedSourceVersion(SourceVersion.RELEASE_7)
//从各个模块传入进来的数据，通过apt传进来
@SupportedOptions(Config.OPTION_VALUE)
public class TestRouterProcessor extends AbstractProcessor {

    /**
     * 初使化注解给提供的工具
     */
    private Elements mElementUtils;  //一个工具，可以损伤类、函数、属性
    private Filer mFiler;         //文件生成器，生成类、资源等
    private Messager mMessager;    //用来打印日志的
    private Types mTypeUtils;   //类信息工具类
    private String mAptPackage;

    /**
     * 初使化工作，提供一些工具
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnvironment.getElementUtils();
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
        mTypeUtils = processingEnvironment.getTypeUtils();
        mAptPackage = processingEnvironment.getOptions().get(Config.OPTION_VALUE);
        mMessager.printMessage(Diagnostic.Kind.NOTE, "===init=====:mAptPackage:" + mAptPackage);
    }

    /**
     * 编译环节会来到这个方法，将被注解的相关信息带过来，根据这些信息生成我们需要的源代码
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mMessager.printMessage(Kind.NOTE, "=====process====1==========");
        if (set.isEmpty()) {
            mMessager.printMessage(Kind.NOTE, "===2=没有发现被TestRouter注解的地方=====");
            return false;
        }

        mMessager.printMessage(Kind.NOTE, "=====process=3=============");
        TypeElement activityType = mElementUtils.getTypeElement(Config.ACTIVITY_PACKAGE);
        TypeMirror activityMirror = activityType.asType();

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(TestRouter.class);
        if(elements==null||elements.isEmpty()){
            mMessager.printMessage(Kind.NOTE, "===4=没有发现被TestRouter注解的地方=====");
            return false;
        }
        // 遍历所有的类节点
        for (Element element : elements) {
            // 获取包名
            String packageName = mElementUtils.getPackageOf(element).getQualifiedName().toString();
            mMessager.printMessage(Kind.NOTE, "=====5======packageName:"+packageName);

            // 获取简单类名，例如：MainActivity
            String className = element.getSimpleName().toString();
            mMessager.printMessage(Kind.NOTE, "=====6===被@TestRouter注解的类有：" + className); // 打印出 就证明APT没有问题

            // 拿到类上的注解
            TestRouter testRouter = element.getAnnotation(TestRouter.class);
            String path = testRouter.path();
            String name = "";
            String group = "";
            if(!path.isEmpty()){
                if(path.contains("/")) {
                    String[] s = path.split("/");
                    group = s[0];
                    name = s[1];
                }
            }
            mMessager.printMessage(Kind.NOTE, "=====7================path:"+testRouter.path());
            mMessager.printMessage(Kind.NOTE, "=====8===============group:"+testRouter.group());
            mMessager.printMessage(Kind.NOTE, "=====8-1===============group2:"+group);


           /* public class HelloJavaPoet {

                public static void main(String[] args) {
                   System.out.println("Hello, JavaPoet~~~~");
                }
            }*/
            //1、先写方法
            MethodSpec main = MethodSpec.methodBuilder("main")
                                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                        .returns(void.class)
                                        .addParameter(System[].class, "args")
                                        .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet ，这是"+group+"模块~")
                                        .build();

            //2、再写类
            TypeSpec helloJavaPoet = TypeSpec.classBuilder("HelloJavaPoet"+name)
                                          .addModifiers(Modifier.PUBLIC)
                                          .addMethod(main)
                                          .build();

            //3、最后写包
            JavaFile packagef = JavaFile.builder(mAptPackage, helloJavaPoet).build();
            mMessager.printMessage(Kind.NOTE, "=====9===============");

            //4、生成文件
            try {
                packagef.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
                mMessager.printMessage(Kind.NOTE, "生成文件时失败，异常:" + e.getMessage());
            }

        }
        return true;
    }
}
