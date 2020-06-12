package com.fangye.annotation.model;

import com.fangye.annotation.enums.RouterType;

import javax.lang.model.element.Element;

/**
 * It contains basic route information.
 */
public class RouterEntity {

    private Element rawType;        // Raw type of route,Activity等
    private Class<?> destination;   // Destination
    private RouterType type;         // Type of route
    private String path;            // Path of route
    private String group;           // Group of route

    private RouterEntity(RouterType typeEnum, /*Element element,*/ Class<?> myClass, String path, String group) {
        this.type = typeEnum;
        // this.element = element;
        this.destination = myClass;
        this.path = path;
        this.group = group;
    }

    // 对外暴露
    // 对外提供简易版构造方法，主要是为了方便APT生成代码
    public static RouterEntity build(RouterType type, Class<?> clazz, String path, String group) {
        return new RouterEntity(type, clazz, path, group);
    }


    // 构建者模式相关
    private RouterEntity(Builder builder) {
        this.type = builder.type;
        this.rawType = builder.element;
        this.destination = builder.clazz;
        this.path = builder.path;
        this.group = builder.group;
    }

    /**
     * 构建者模式
     */
    public static class Builder {

        // 枚举类型：Activity
        private RouterType type;
        // 类节点
        private Element element;
        // 注解使用的类对象
        private Class<?> clazz;
        // 路由地址
        private String path;
        // 路由组
        private String group;

        public Builder addType(RouterType type) {
            this.type = type;
            return this;
        }

        public Builder addElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder addClazz(Class<?> clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder addPath(String path) {
            this.path = path;
            return this;
        }

        public Builder addGroup(String group) {
            this.group = group;
            return this;
        }

        // 最后的build或者create，往往是做参数的校验或者初始化赋值工作
        public RouterEntity build() {
            if (path == null || path.length() == 0) {
                throw new IllegalArgumentException("path必填项为空，如：/app/MainActivity");
            }
            return new RouterEntity(this);
        }
    }



    public Element getRawType() {
        return rawType;
    }

    public void setRawType(Element rawType) {
        this.rawType = rawType;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }

    public RouterType getType() {
        return type;
    }

    public void setType(RouterType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "RouterEntity{" +
                "rawType=" + rawType +
                ", destination=" + destination +
                ", type=" + type +
                ", path='" + path + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
