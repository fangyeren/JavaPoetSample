package com.fangye.compiler;

/**
 * 全局常量
 */
public class Config {

    /**
     * apt传递过来的数据
     */
    public static final String OPTION_MODULE_NAME = "moduleName";
    public static final String OPTION_PACKAGE_NAME = "packageNameForAPT";

    /**
     * 注解全类名
     */
    public static final String ANNOTATION_TEST_TYPE = "com.fangye.annotation.TestRouter";
    public static final String ANNOTATION_EROUTER_TYPE = "com.fangye.annotation.ERouter";
    public static final String ANNOTATION_EAUTOWIRED_TYPE = "com.fangye.annotation.EAutowired";

    /**
     * 系统已知类型的全类名
     */
    //Activity全类名
    public static final String ACTIVITY_PACKAGE = "android.app.Activity";
    // String全类名
    public static final String STRING = "java.lang.String";


    /**
     * 提供给外界使用的接口，是一种标准，定义到api模块中，complier module不会依赖 erouterapi ,故通过全类名反射来获取
     */
    // ERouter api 包名
    private static final String EROUTER_API_PACKAGE = "com.fangye.erouterapi.template";

    // ERouter api 的 IRouterGroup 标准
    public static String EROUTER_API_GROUP = EROUTER_API_PACKAGE + ".IRouterGroup";

    // ERouter api 的 IRouterPath 标准
    public static String EROUTER_API_PATH = EROUTER_API_PACKAGE + ".IRouterPath";

    // ERouter api 的 IAutowired ，用于Activity 参数传递
    public static String EROUTER_API_PARAMETER = EROUTER_API_PACKAGE + ".IAutowired";


    /**
     * 生成文件中对应局部变量名称
     */
    //生成path模板代码 方法中的变量引用
    public static final String EROUTER_PATH_METHOD_VAR = "pathMap";
    //生成group模板代码 方法中的变量引用
    public static final String EROUTER_GOUP_METHOD_VAR = "groupMap";
    //生成 Autowired 模板代码方法中参数引用变量
    public static final String EROUTER_AUTOWIRED_PARAMETER_VAR = "target";


    /**
     * 需要生成的文件名对应的前缀或者后缀
     */
    // 实现IRouterGroup 生成文件名称前缀为 ARouter$$Path$$
    public static final String EROUTER_PATH_FILE_NAME = "ERouter$$Path$$";
    // 实现IRouterGroup 生成文件名称前缀为 ARouter$$Group$$
    public static final String EROUTER_GROUP_FILE_NAME = "ERouter$$Group$$";
    // 实现 IAutowired 生成文件名称 后缀为 $$Autowired （代表的注入传递参数的值）
    public static final String EROUTER_AUTOWIRED_FILE_NAME = "$$Autowired";

    /**
     * apt 生成文件的文档注释
     */
    private static final String EROUTER_WARNING_TIPS = "DO NOT EDIT THIS FILE!!! IT WAS GENERATED BY EROUTER.";
    public static final String EROUTER_GROUP_WARNING_TIPS = "create group file by apt \n "+EROUTER_WARNING_TIPS;
    public static final String EROUTER_PATH_WARNING_TIPS = "create path file by apt \n "+EROUTER_WARNING_TIPS;
    public static final String EROUTER_AUTOWIRED_WARNING_TIPS = "create autowired file by apt \n "+EROUTER_WARNING_TIPS;
}