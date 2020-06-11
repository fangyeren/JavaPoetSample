package com.fangye.business.entity;

/**
 * 路径的实体类
 */
public class PathEntity {
    private String path;
    private Class clazz;

    public PathEntity(String path, Class clazz) {
        this.path = path;
        this.clazz = clazz;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "PathEntity{" +
                "path='" + path + '\'' +
                ", clazz=" + clazz +
                '}';
    }
}
