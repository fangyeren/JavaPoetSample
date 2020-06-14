package com.fangye.erouterapi.launcher;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.fangye.annotation.model.RouterEntity;
import com.fangye.erouterapi.template.IRouterGroup;
import com.fangye.erouterapi.template.IRouterPath;

/**
 * 路由管理类（双重检查 单例）
 */
public class ERouterManager {

    private static final String TAG = "ERouterManager";
    private String group; // 路由的group , app/study/intelligent/user/tutor/college
    private String path;  // 路由的path  例如：/study/study_main

    private LruCache<String, IRouterGroup> groupLruCache;
    private LruCache<String, IRouterPath> pathLruCache;

    // FILE_GROUP_NAME 和 compiler 模块定义的必须一致，否则对应不上，
    // 最终需要找到的文件为：ERouter$$Group$$study
    private final static String FILE_GROUP_NAME = "ERouter$$Group$$";

    private volatile static ERouterManager instance;

    private ERouterManager() {
        groupLruCache = new LruCache<>(100);
        pathLruCache = new LruCache<>(100);
    }

    public static ERouterManager getInstance() {
        if (instance == null) {
            synchronized (ERouterManager.class) {
                if (instance == null) {
                    instance = new ERouterManager();
                }
            }
        }
        return instance;
    }



    /***
     * 判断path 是否符合标准
     * @param path 例如：/study/study_main
     *      * @return
     */
    public PostCard build(String path) {
        if (TextUtils.isEmpty(path) || !path.startsWith("/")) {
            throw new IllegalArgumentException("path不能为空，并且必须以“/”开头，如 /study/study_main");
        }

        if (path.lastIndexOf("/") == 0) {
            throw new IllegalArgumentException("必须有两个以上的”/“：如 /study/study_main");
        }

        // 截取group ,  /order/Order_MainActivity  finalGroup=order
        String finalGroup = path.substring(1, path.indexOf("/", 1)); // finalGroup = order

        if (TextUtils.isEmpty(finalGroup)) {
            throw new IllegalArgumentException("截取path得到group时出错，请检查路由路径是否正确：如 /study/study_main");
        }

        this.path =  path;
        this.group = finalGroup;

        return new PostCard();
    }

    /**
     * 跳转
     * @param context
     * @param bundle
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Object navigation(Context context, PostCard bundle) {

        String groupClassName = context.getPackageName() + "." + FILE_GROUP_NAME + upperCaseFirst(group);
        Log.e(TAG, "=========groupClassName=" + groupClassName);

        try {

            // 读取路由组Group类文件
            IRouterGroup loadGroup = groupLruCache.get(group);
            // 读取路由组Group类文件
            if (null == loadGroup) { // 缓存里面没有东东
                // 加载APT路由组Group类文件 例如：ARouter$$Group$$order
                Class<?> aClass = Class.forName(groupClassName);
                // 初始化类文件
                loadGroup = (IRouterGroup) aClass.newInstance();

                // 保存到缓存
                groupLruCache.put(group, loadGroup);
            }

            if (loadGroup.getGroupMap().isEmpty()) {
                throw new RuntimeException("路由表Group报废了...");
            }


            // 读取路由Path类文件
            IRouterPath loadPath = pathLruCache.get(path);
            if (null == loadPath) { // 缓存里面没有东东 Path
                // 1.invoke loadGroup
                // 2.Map<String, Class<? extends ARouterLoadPath>>
                Class<? extends IRouterPath> clazz = loadGroup.getGroupMap().get(group);
                // 3.从map里面获取 ARouter$$Path$$order.class
                loadPath = clazz.newInstance();

                // 保存到缓存
                pathLruCache.put(path, loadPath);
            }

            if (loadPath != null) {
                if (loadPath.getPathMap().isEmpty()) {
                    throw new RuntimeException("路由表Path报废了...");
                }

                // 我们已经进入 PATH 函数 ，开始拿 Class 进行跳转
                RouterEntity routerBean = loadPath.getPathMap().get(path);
                if (routerBean != null) {
                    switch (routerBean.getType()) {
                        case ACTIVITY:
                            Intent intent = new Intent(context, routerBean.getDestination());
                            intent.putExtras(bundle.getBundle());
                            context.startActivity(intent);
                            break;

                        // 后面可以扩展其它情况
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public class PostCard {

        // 携带的值，保存到这里  Intent 传输
        private Bundle bundle = new Bundle();

        public Bundle getBundle() {
            return this.bundle;
        }

        // 对外界提供，可以携带参数的方法
        public PostCard withString(@NonNull String key, @Nullable String value) {
            bundle.putString(key, value);
            return this;
        }

        public PostCard withBoolean(@NonNull String key, @Nullable boolean value) {
            bundle.putBoolean(key, value);
            return this;
        }

        public PostCard withInt(@NonNull String key, @Nullable int value) {
            bundle.putInt(key, value);
            return this;
        }

        public PostCard withBundle(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }


        /**
         *  完成跳转
         * @param context Context
         * @return
         */
        public Object navigation(Context context) {
            // 单一原则 , 把自己所有行为 都交给了  路由管理器
            return ERouterManager.getInstance().navigation(context, this);
        }
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
