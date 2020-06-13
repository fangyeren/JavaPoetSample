package com.fangye.annotation.enums;

/**
 * 注解的类型，目前只支持ACTIVITY
 */
public enum RouterType {
    ACTIVITY(0, "android.app.Activity"),
    UNKNOWN(-1, "Unknown route type");

    int id;
    String className;
    public int getId() {
        return id;
    }

    public RouterType setId(int id) {
        this.id = id;
        return this;
    }

    public static String getClassName(int index) {
        for (RouterType c : RouterType.values()) {
            if (c.getId() == index) {
                return c.className;
            }
        }
        return null;
    }

    public String getClassName() {
        return className;
    }

    public RouterType setClassName(String className) {
        this.className = className;
        return this;
    }

    RouterType(int id, String className) {
        this.id = id;
        this.className = className;
    }

    public static RouterType parse(String name) {
        for (RouterType routeType : RouterType.values()) {
            if (routeType.getClassName().equals(name)) {
                return routeType;
            }
        }

        return UNKNOWN;
    }
}
