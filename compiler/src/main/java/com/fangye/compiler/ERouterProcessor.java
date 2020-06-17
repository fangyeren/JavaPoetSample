package com.fangye.compiler;

import com.fangye.annotation.ERouter;
import com.fangye.annotation.TestRouter;
import com.fangye.annotation.enums.RouterType;
import com.fangye.annotation.model.RouterEntity;
import com.fangye.compiler.utils.Utils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

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
// 向javac 注册我们自定义的注解处理器，这样在javac编译时，才会调用到我们这个自定义的注解处理器方法 process
// AutoService 会自动在META-INF/service文件下生成Processor的配置文件，
// 该文件中就是实现该服务接口的具体实现类，当外部程序使用这个模块时，就能通过META-INF/service里的配置文件找到具体的实现类
// 并加载实例化，完成模块的注入
@AutoService(Processor.class)
//监控某个注解，如果只有一个就不用写“{}”，具体的注解类放到Config中，方便管理
@SupportedAnnotationTypes({Config.ANNOTATION_EROUTER_TYPE})
// 使用的源代码版本
@SupportedSourceVersion(SourceVersion.RELEASE_7)
//从各个模块传入进来的数据，通过apt传进来
@SupportedOptions({Config.OPTION_MODULE_NAME, Config.OPTION_PACKAGE_NAME})
public class ERouterProcessor extends AbstractProcessor {

    /**
     * 初使化注解给提供的工具
     */
    private Elements mElementUtils;  //一个工具，可以损伤类、函数、属性
    private Filer mFiler;         //文件生成器，生成类、资源等
    private Messager mMessager;    //用来打印日志的
    private Types mTypeUtils;   //类信息工具类
    private String mAptPackage;  // apt生成文件包名
    private String mOptionModuleName;  //moduleName

    // 仓库一  PATH
    private Map<String, List<RouterEntity>> mAllPathMap = new HashMap<>();

    // 仓库二 GROUP
    private Map<String, String> mAllGroupMap = new HashMap<>();

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
        mOptionModuleName = processingEnvironment.getOptions().get(Config.OPTION_MODULE_NAME);
        mAptPackage = processingEnvironment.getOptions().get(Config.OPTION_PACKAGE_NAME);
        mMessager.printMessage(Kind.NOTE, "ERouter:===init=====:mAptPackage:" + mAptPackage + "\nmOptionModuleName:" + mOptionModuleName);
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

        if (set.isEmpty()) {
            return false;
        }
        mMessager.printMessage(Kind.NOTE, "ERouter:====1======RouterType=======" + RouterType.ACTIVITY.getClassName());
        TypeElement activityType = mElementUtils.getTypeElement(RouterType.ACTIVITY.getClassName());
        TypeMirror activityMirror = activityType.asType();

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ERouter.class);
        if (elements == null || elements.isEmpty()) {
            mMessager.printMessage(Kind.NOTE, "ERouter:==2=没有发现被ERouter注解的地方=====");
            return false;
        }
        // 遍历所有的类节点
        for (Element element : elements) {

            /**
             * 获取包名和类名
             */
            // 获取包名
            String packageName = mElementUtils.getPackageOf(element).getQualifiedName().toString();
            mMessager.printMessage(Kind.NOTE, "ERouter:======3====packageName:" + packageName);
            // 获取简单类名，例如：StudyMainActivity
            String className = element.getSimpleName().toString();
            mMessager.printMessage(Kind.NOTE, "ERouter:=====4===被@ERouter注解的类有：" + className); // 打印出 就证明APT没有问题

            /**
             * 拿到类上的注解
             */
            ERouter erouter = element.getAnnotation(ERouter.class);
            String path = erouter.path();
            mMessager.printMessage(Kind.NOTE, "ERouter:==5=拿到Erouter的注解信息======path:" + path);


            /**
             * 创建routerEntity
             */
            RouterEntity routerEntity = new RouterEntity.Builder()
                    .addGroup(erouter.group())
                    .addPath(erouter.path())
                    .addElement(element)
                    .build();
            mMessager.printMessage(Kind.NOTE, "ERouter:=6=routerEntity======routerEntity:" + routerEntity.toString() + "\n" + " ");

            /**
             * 判断当前是不是activity
             */
            //获取当前的类型
            TypeMirror elementMirror = element.asType();
            //判断当前是不是Activity
            if (mTypeUtils.isSubtype(elementMirror, activityMirror)) {
                routerEntity.setType(RouterType.ACTIVITY);
            } else {
                throw new RuntimeException("@ERouter:====注解暂用于Activity类之上");
            }
            mMessager.printMessage(Kind.NOTE, "ERouter:=7==判断当前是不是activity=====" + elementMirror.toString() + "\n" + " ");


            mMessager.printMessage(Kind.NOTE, "ERouter:=8==校验 path  group  用户传递过来的=====\n" + "\n" + " ");

            // 校验 path  group  用户传递过来的
            if (checkERouterPath(routerEntity)) {
                //打印检查通过信息
                mMessager.printMessage(Kind.NOTE, "ERouter:=9==校验 path  group  成功=====\n");

                // PATH 仓库一
                List<RouterEntity> routerBeans = mAllPathMap.get(routerEntity.getGroup());

                // 如果从Map中找不到key为：bean.getGroup()的数据，就新建List集合再添加进Map
                if (Utils.isEmpty(routerBeans)) {
                    routerBeans = new ArrayList<>();
                    routerBeans.add(routerEntity);
                    mAllPathMap.put(routerEntity.getGroup(), routerBeans);
                } else { // 从Map中找到key中返回的Value值routerBeans，就直接添加进去
                    routerBeans.add(routerEntity);
                }
            } else {
                mMessager.printMessage(Diagnostic.Kind.ERROR, "ERouter:==10=校验 path  group失败，@ARouter注解未按规范配置，如：/study/study_main" + "\n" + " ");
            }
        } // for 结束


        /**
         * 拿到path 和 group的接口（path和group在api模块中，内部通过反射完成的）
         */
        TypeElement pathType = mElementUtils.getTypeElement(Config.EROUTER_API_PATH);
        TypeElement groupType = mElementUtils.getTypeElement(Config.EROUTER_API_GROUP);
        mMessager.printMessage(Kind.NOTE, "ERouter:=11=通过反射拿到path 和 group的接口=====pathType:" + pathType + "\ngroupType" + groupType + "\npath:" + Config.EROUTER_API_PATH + "\ngroup:" + Config.EROUTER_API_GROUP + "\n" + " ");


        /**
         * 生成Path
         */
        mMessager.printMessage(Kind.NOTE, "ERouter:=12=生成Path=====" + "\n" + " ");
        try {
            createPathFile(pathType);
        } catch (IOException e) {
            e.printStackTrace();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "ERouter:=13===生成PATH发生了异常 e:" + e.getMessage() + "\n" + " ");
        }

        /**
         * 生成group
         */
        mMessager.printMessage(Kind.NOTE, "ERouter:=14=生成group====" + "\n" + " ");
        try {
            createGroupFile(groupType, pathType);
        } catch (IOException e) {
            e.printStackTrace();
            mMessager.printMessage(Kind.NOTE, "ERouter:=15=生成GROUP发生了异常====e:" + e.toString() + "\n" + " ");
        }


        return true;
    }

    /**
     * 创建 group 文件
     *
     * @param groupType TypeElement
     * @param pathType  TypeElement
     *                  <p>
     *                  模板代码：
     *                  //public class ERouter$$Group$$Study implements IRouterGroup {
     *                  //    @Override
     *                  //    public Map<String, Class<? extends IRouterPath>> getGroupMap() {
     *                  //        Map<String, Class<? extends IRouterPath>> groupMap = new HashMap<>();
     *                  //        groupMap.put("study", ERouter$$Path$$Study.class);
     *                  //        return groupMap;
     *                  //    }
     *                  //}
     */
    private void createGroupFile(TypeElement groupType, TypeElement pathType) throws IOException {
        // 判断是否有 要生成的 group文件，没有则返回
        if (Utils.isEmpty(mAllGroupMap) || Utils.isEmpty(mAllPathMap)) {
            return;
        }
        /**
         * 一、先写方法
         */
        //1、先写方法上的 return内容 ，即Map<String, Class<? extends IRouterPath>>
        TypeName methodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),   //Map
                ClassName.get(String.class), //第一个参数
                ParameterizedTypeName.get(// 第二个参数：Class<? extends IRouterPath>
                        ClassName.get(Class.class), //某class 是否属于 IRouterPath的实现类
                        WildcardTypeName.subtypeOf(ClassName.get(pathType)))); //IRouterPath

        //2、写方法
        // @Override
        // public Map<String, Class<? extends IRouterPath>> getGroupMap() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getGroupMap")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(methodReturn);

        //3、写方法第一行内容
        //Map<String, Class<? extends IRouterPath>> groupMap = new HashMap<>();
        methodBuilder.addStatement("$T<$T, $T> $N = new $T<>()", //模板代码
                ClassName.get(Map.class),  //map
                ClassName.get(String.class),  //key
                ParameterizedTypeName.get(   // value : Class<? extends IRouterPath>
                        ClassName.get(Class.class), //某某class 是属于 IRouterPath的实现类
                        WildcardTypeName.subtypeOf(ClassName.get(pathType))), //IRouterPath
                Config.EROUTER_GOUP_METHOD_VAR,    //变量
                ClassName.get(HashMap.class) //hashmap
        );

        //4、写方法的其它内容
        //groupMap.put("personal", ERouter$$Path$$study.class);

        for (Map.Entry<String, String> entry : mAllGroupMap.entrySet()) {
            mMessager.printMessage(Kind.NOTE, "ERouter:=14-1=====key:" + entry.getKey() + "\nvalue:" + entry.getValue());

            // groupMap.put("study", ERouter$$Path$$study.class);
            methodBuilder.addStatement("$N.put($S, $T.class)",  //模板
                    Config.EROUTER_GOUP_METHOD_VAR,       // groupMap.put
                    entry.getKey(),   //path
                    ClassName.get(mAptPackage, entry.getValue())); // 类文件在指定包名下
        }

        //5、return groupMap;
        methodBuilder.addStatement("return $N", Config.EROUTER_GOUP_METHOD_VAR);


        /**
         * 二、写类，添加上一步生成的方法
         */
        // 最终生成的类文件名   ERouter$$Group$$ + study
        String finalClassName = Config.EROUTER_GROUP_FILE_NAME + upperCaseFirst(mOptionModuleName);
        mMessager.printMessage(Diagnostic.Kind.NOTE, "ERouter:===APT生成路由组Group类文件：" +
                mAptPackage + "." + finalClassName);

        TypeSpec typeSpec = TypeSpec.classBuilder(finalClassName) // 类名
                .addJavadoc(Config.EROUTER_GROUP_WARNING_TIPS)   //添加注释
                .addSuperinterface(ClassName.get(groupType)) // 实现IRouterGroup接口
                .addModifiers(Modifier.PUBLIC) // public修饰符
                .addMethod(methodBuilder.build()) // 方法的构建（方法参数 + 方法体）
                .build();


        /**
         * 三、写包，并生成文件：ARouter$$Group$$Study
         */
        //public class ERouter$$GroupStudy implements IRouterGroup {
        JavaFile.builder(mAptPackage, typeSpec)  // 包和类
                .build()         // JavaFile构建完成
                .writeTo(mFiler); // 文件生成器开始生成类文件

    }

    /**
     * 创建path文件
     *
     * @param pathType  TypeElement
     * @throws IOException  IOException
     * 模板代码：
     *                     //public class ERouter$$Path$$Study implements IRouterPath {
     *                     //    @Override
     *                     //    public Map<String, RouterEntity> getPathMap() {
     *                     //        Map<String, RouterEntity> pathMap = new HashMap<>();
     *                     //        pathMap.put("/study/study_details", RouterEntity.build(RouterType.ACTIVITY, StudyDetailsActivity.class, "/study/study_details", "study"));
     *                     //        pathMap.put("/study/study_main", RouterEntity.build(RouterType.ACTIVITY, StudyMainActivity.class, "/study/study_main", "study"));
     *                     //        return pathMap;
     *                     //    }
     *                     //}
     */
    private void createPathFile(TypeElement pathType) throws IOException {
        //判断 mAllPathMap 是否有数据，有则代表需要生成文件,否则返回
        if (Utils.isEmpty(mAllPathMap)) {
            return;
        }


        /**
         * 一、先写方法
         */
        //1、先写方法上的 return内容 ，即Map<String, RouterEntity>
        TypeName methodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterEntity.class));

        // 2.给方法添加修饰，如名称、Override、publish ,及上一步生成的返回值 methodReturn
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getPathMap")
                .addAnnotation(Override.class) // 给方法上添加注解
                .addModifiers(Modifier.PUBLIC) // public修饰符
                .returns(methodReturn);   //上一步生成的methodReturn

        // 3、添加方法里的第一行语句： Map<String, RouterBean> pathMap = new HashMap<>();
        methodBuilder.addStatement("$T<$T, $T> $N = new $T<>()",
                ClassName.get(Map.class),             // Map
                ClassName.get(String.class),          // String
                ClassName.get(RouterEntity.class),    // RouterBean
                Config.EROUTER_PATH_METHOD_VAR,       //变量名，请看上面的注释
                ClassName.get(HashMap.class)
        );

        //4、给方法添加其它内容，因为一个group中可能有很多个RouterEntity，需要循环添加
        for (Map.Entry<String, List<RouterEntity>> entry : mAllPathMap.entrySet()) {
            List<RouterEntity> pathList = entry.getValue();
            mMessager.printMessage(Kind.NOTE, "ERouter:=12-1======key:" + entry.getKey() + "\nvalue:" + pathList.size());
            for (RouterEntity routerBean : pathList) {
                mMessager.printMessage(Kind.NOTE, "ERouter:=12-2======routerBean:" + routerBean.toString());

                /**
                 * 模板
                 */
                //        pathMap.put("/study/study_main",
                //                RouterEntity.build(RouterType.ACTIVITY,
                //                        StudyMainActivity.class,
                //                        "/study/study_main",
                //                        "study"));

                // 给方法添加代码
                methodBuilder.addStatement(
                        "$N.put($S, $T.build($T.$L, $T.class, $S, $S))", //语句模板
                        Config.EROUTER_PATH_METHOD_VAR,            // pathMap.put
                        routerBean.getPath(),              // "/study/study_main"
                        ClassName.get(RouterEntity.class), // RouterEntity
                        ClassName.get(RouterType.class),    //   RouterType
                        routerBean.getType(),               // 枚举类型：ACTIVITY
                        ClassName.get((TypeElement) routerBean.getRawType()), // StudyMainActivity.class
                        routerBean.getPath(),               // 路径名
                        routerBean.getGroup()               // 组名
                );
                mMessager.printMessage(Kind.NOTE, "ERouter:=12-3======" + "\n" + " ");

            } // for end
            mMessager.printMessage(Kind.NOTE, "ERouter:=12-4======" + "\n" + " ");
            // 5、生成return 语句： return pathMap;
            methodBuilder.addStatement("return $N", Config.EROUTER_PATH_METHOD_VAR);
            mMessager.printMessage(Kind.NOTE, "ERouter:=12-5======" + "\n" + " ");
            /**
             * 二、写类，添加上一步生成的方法
             */
            // 最终生成的类文件名  ERouter$$Path$$  + study
            String finalClassName = Config.EROUTER_PATH_FILE_NAME + upperCaseFirst(entry.getKey());
            mMessager.printMessage(Kind.NOTE, "ERouter:=12-6======" + finalClassName + "\n" + " " + " " + " ");
            TypeSpec typeSpec = TypeSpec.classBuilder(finalClassName) // 类名
                    .addJavadoc(Config.EROUTER_PATH_WARNING_TIPS)   //添加注释
                    .addSuperinterface(ClassName.get(pathType)) // 实现ARouterLoadPath接口
                    .addModifiers(Modifier.PUBLIC) // public修饰符
                    .addMethod(methodBuilder.build()) // 方法的构建（方法参数 + 方法体）
                    .build();
            mMessager.printMessage(Kind.NOTE, "ERouter:=12-7======" + "\n" + " " + " ");
            /**
             * 三、写包，添加上一步生成的类，并创建文件
             */
            JavaFile.builder(mAptPackage, typeSpec) // 包和类
                    .build()            // JavaFile构建完成
                    .writeTo(mFiler); // 文件生成器开始生成类文件
            mMessager.printMessage(Kind.NOTE, "ERouter:=12-8======" + "\n" + " " + " ");
            // 告诉Group
            mAllGroupMap.put(entry.getKey(), finalClassName);
        }
    }


    /**
     * 校验@ERouter注解的值，如果group未填写就从必填项path中截取数据
     *
     * @param bean 路由详细信息，最终实体封装类
     */
    private final boolean checkERouterPath(RouterEntity bean) {
        String group = bean.getGroup();
        String path = bean.getPath();

        // @ERouter注解中的path值，必须要以 / 开头（模仿阿里Arouter规范）
        if (Utils.isEmpty(path) || !path.startsWith("/")) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "@ERouter注解中的path值，必须要以 / 开头");
            return false;
        }

        // @ERouter注解中的path值，必须超过两个/,如/study_main，不让通过
        if (path.lastIndexOf("/") == 0) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "EARouter注解未按规范配置，如：/study/study_main");
            return false;
        }

        // 从第一个 / 到第二个 / 中间截取，如：/study/study_main 截取出 study 作为group
        // finalGroup == app, study, user,tutor,intelligent
        String finalGroup = path.substring(1, path.indexOf("/", 1));


        // @ARouter注解中的group有赋值情况   用户传递进来 study，  我截取出来的也必须是 study
        if (!Utils.isEmpty(group) && !group.equals(mOptionModuleName)) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "@ERouter注解中的group值必须和子模块名一致！");
            return false;
        } else {
            // 给group赋值
            bean.setGroup(finalGroup);
        }

        return true;
    }

    private static String upperCaseFirst(String str) {
        return upperCase(str.substring(0, 1)) + str.substring(1);
    }


    private static String upperCase(String str) {

        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

}
