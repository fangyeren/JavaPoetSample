package com.fangye.compiler;

import com.fangye.annotation.ERouter;

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
     * 提供给外界使用的接口，是一种标准，定义到api模块中
     */
    // ERouter api 包名
    private static final String EROUTER_API_PACKAGE = "com.fangye.erouterapi.template";

    // ERouter api 的 IRouterGroup 高层标准
    public static String EROUTER_API_GROUP = EROUTER_API_PACKAGE + ".IRouterGoup";

    // ERouter api 的 IRouterPath 高层标准
    public static String EROUTER_API_PATH = EROUTER_API_PACKAGE + ".IRouterPath";

    // ERouter api 的 ISyringe ，用于Activity 参数传递
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
     * 需要生成的文件名对应的前缀
     */
    // 实现IRouterGroup 生成文件名称前缀为 ARouter$$Path$$
    public static final String EROUTER_PATH_FILE_NAME = "ARouter$$Path$$";
    // 实现IRouterGroup 生成文件名称前缀为 ARouter$$Group$$
    public static final String EROUTER_GROUP_FILE_NAME = "ARouter$$Group$$";
    // 实现 IAutowired 生成文件名称 后缀为 $$Autowired （代表的注入传递参数的值）
    public static final String EROUTER_AUTOWIRED_FILE_NAME = "$$Autowired";
}
