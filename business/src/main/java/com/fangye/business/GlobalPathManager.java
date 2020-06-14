package com.fangye.business;

import com.fangye.business.entity.PathEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局路径管理类（枚举单例）
 */
//public class   GlobalPathManager {

/**
 * //1、单例模式一，枚举
 */
public enum GlobalPathManager {
    INSTANCE;



    /**
     * 2、单例模式二，双重检查
     * @return
     */
//    private volatile static GlobalPathManager instance;
//    private GlobalPathManager() {
//    }
//    public static GlobalPathManager getInstance() {
//        if (instance == null) {
//            synchronized (GlobalPathManager.class) {
//                if (instance == null) {
//                    instance = new GlobalPathManager();
//                }
//            }
//        }
//        return instance;
//    }

    /**
     * 3、单例模式三、静态内部类
     */
//    private static class SingletonInstance {
//
//        private static final GlobalPathManager INSTANCE = new GlobalPathManager();
//    }
//
//    public static GlobalPathManager getInstance() {
//        return SingletonInstance.INSTANCE;
//    }


    /**
     * String 代表group , 即学习中心、个人中心、口语家教
     * List<PathEntity> 代表模块里的路径信息，一个模块中可以有多个路径
     */
    private  Map<String, List<PathEntity>> sGroupMap = new HashMap<>();

    public  void putPathData(String groupName, String pathName, Class<?> clazz) {
        List<PathEntity> list = sGroupMap.get(groupName);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(new PathEntity(pathName, clazz));
        sGroupMap.put(groupName, list);
    }

    /**
     * 根据组名和路径名获取类对象，达到跳转目的
     *
     * @param groupName 组名，即学习中心、个人中心、口语家教
     * @param pathName  路径名，
     * @return 跳转目标的class类对象
     */
    public  Class<?> getTargetClass(String groupName, String pathName) {
        List<PathEntity> list = sGroupMap.get(groupName);
        if (list == null) return null;
        for (PathEntity path : list) {
            if (pathName.equalsIgnoreCase(path.getPath())) {
                return path.getClazz();
            }
        }
        return null;
    }

    public  void remove() {
        sGroupMap.clear();
        sGroupMap = null;
        System.gc();
    }

}
