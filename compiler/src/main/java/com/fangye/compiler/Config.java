package com.fangye.compiler;

public class Config {
    public static final String OPTION_MODULE_NAME = "moduleName";
    public static final String OPTION_PACKAGE_NAME = "packageNameForAPT";
    public static final String ANNOTATION_TEST_TYPE = "com.fangye.annotation.TestRouter";
    public static final String ANNOTATION_EROUTER_TYPE = "com.fangye.annotation.ERouter";
    public static final String ACTIVITY_PACKAGE = "android.app.Activity";


    // ERouter api 包名
    private static final String EROUTER_API_PACKAGE = "com.fangye.erouterapi.template";

    // ERouter api 的 IRouterGroup 高层标准
    public static String EROUTER_API_GROUP = EROUTER_API_PACKAGE + ".IRouterGoup";

    // ERouter api 的 IRouterPath 高层标准
    public static String EROUTER_API_PATH = EROUTER_API_PACKAGE + ".IRouterPath";


    public static final String EROUTER_PATH_METHOD_VAR = "pathMap";
    public static final String EROUTER_GOUP_METHOD_VAR = "groupMap";

    // 路由组，PATH 最终要生成的 文件名
    public static final String EROUTER_PATH_FILE_NAME = "ARouter$$Path$$";
    public static final String EROUTER_GROUP_FILE_NAME = "ARouter$$Group$$";

}
