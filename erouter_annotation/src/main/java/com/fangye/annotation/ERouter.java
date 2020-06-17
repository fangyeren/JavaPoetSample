package com.fangye.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 */
//代码注解需要添加到类上
@Target(ElementType.TYPE)
//class 代表的是编译环节
@Retention(RetentionPolicy.CLASS)
public @interface ERouter {
    /**
     *   路径信息
     * @return
     */
    String path();

    /**
     * 组信息，这里我们可以认为是模块名称，比如我们的个人中心、学生中心模块、智能备考、口语家教等模块
     * @return
     */
    String group() default "";
}
