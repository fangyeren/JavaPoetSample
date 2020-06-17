package com.fangye.erouterapi.launcher;

import android.app.Activity;
import android.util.LruCache;

import com.fangye.erouterapi.template.IAutowired;

/**
 * 参数的 加载管理器(静态内部类 单例)
 */
public class AutowiredManager {

    // FILE_SUFFIX_NAME 和 compiler 模块定义的必须一致，否则对应不上，
    // 最终需要找到的文件为：StudyMainActivity + $$Autowired
    private static final String FILE_SUFFIX_NAME = "$$Autowired";

    private static class SingletonInstance {
        private static final AutowiredManager INSTANCE = new AutowiredManager();
    }

    public static AutowiredManager getInstance() {
        return SingletonInstance.INSTANCE;
    }


    // 懒加载  用到了 就加载，    阿里的路由 全局加载（不管你用没有用到，全部加载）
    // LRU缓存 key=类名      value= 参数加载接口
    private LruCache<String, IAutowired> cache;

    private AutowiredManager() {
        //定义100 个，超过后，会将最少使用 移除
        cache = new LruCache<>(100);
    }


    /**
     * 使用时需要在oncreate 中 注册此方法
     * @param activity Activity
     */
    public void inject(Activity activity) {
        //StudyMainActivity
        String className = activity.getClass().getName();

        //通过key获取到 IAutowired
        IAutowired autowired = cache.get(className);
        if (autowired == null) {
            try {
                Class<?> aClass = Class.forName(className + FILE_SUFFIX_NAME);
                autowired = (IAutowired) aClass.newInstance();
                // 保存到缓存
                cache.put(className, autowired);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        autowired.inject(activity);
    }
}
