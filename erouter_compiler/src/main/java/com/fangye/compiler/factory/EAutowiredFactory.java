package com.fangye.compiler.factory;

import com.fangye.annotation.EAutowired;
import com.fangye.compiler.Config;
import com.fangye.compiler.utils.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

/**
 * 参数工厂 ，用来生成 方法内容的
 *
 */
public class EAutowiredFactory {

    private MethodSpec.Builder method;

    // 类名，如：StudyMainActivity   StudyMainActivity
    private ClassName className;

    // Messager从注解处理器串传入的 ，用来报告错误，警告和其他提示信息 ，
    private Messager messager;

    private EAutowiredFactory(Builder builder) {
        this.messager = builder.messager;
        this.className = builder.className;

        // 通过方法参数体构建方法体：
        // @Override
        // public void inject(Object target) {
        method = MethodSpec.methodBuilder("inject")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(builder.parameterSpec);
    }

    /**
     *  // 只有一行
     * StudyMainActivity t = (StudyMainActivity)target;
     */
    public void addFirstStatement() {
        method.addStatement("$T t = ($T) " + Config.EROUTER_AUTOWIRED_PARAMETER_VAR, className, className);
    }

    public MethodSpec build() {
        return method.build();
    }

    /**
     * // 多行 循环
     * 构建方体内容，如：t.s = t.getIntent.getStringExtra("s");
     * @param element 被注解的属性元素
     */
    public void buildStatement(Element element) {
        // 遍历注解的属性节点 生成函数体
        TypeMirror typeMirror = element.asType();
        // 获取 TypeKind 枚举类型的序列号
        int type = typeMirror.getKind().ordinal();
        // 获取属性名
        String fieldName = element.getSimpleName().toString();
        // 获取注解的值
        String annotationValue = element.getAnnotation(EAutowired.class).name();
        // 判断注解的值为空的情况下的处理（注解中有name值就用注解值）
        annotationValue = Utils.isEmpty(annotationValue) ? fieldName : annotationValue;
        // 最终拼接的前缀 t，代表activity ,filedName 代表field 即可以给动态给上个页面传过来的数据赋值
        String finalValue = "t." + fieldName;
        // t.s = t.getIntent().
        String methodContent = finalValue + " = t.getIntent().";

        // TypeKind 枚举类型不包含String
        if (type == TypeKind.INT.ordinal()) {
            // t.s = t.getIntent().getIntExtra("age", t.age);
            methodContent += "getIntExtra($S, " + finalValue + ")";
        } else if (type == TypeKind.BOOLEAN.ordinal()) {
            // t.s = t.getIntent().getBooleanExtra("isSuccess", t.age);
            methodContent += "getBooleanExtra($S, " + finalValue + ")";
        } else { // String
            // t.s = t.getIntent.getStringExtra("s");
            if (typeMirror.toString().equalsIgnoreCase(Config.STRING)) {
                methodContent += "getStringExtra($S)";
            }
        }

        if (methodContent.endsWith(")")) {
            // 添加最终拼接方法内容语句
            method.addStatement(methodContent, annotationValue);
        } else {
            messager.printMessage(Diagnostic.Kind.ERROR, "目前暂支持String、int、boolean传参");
        }
    }

    /**
     * 为了完成Builder构建者设计模式
     */
    public static class Builder {

        // Messager用来报告错误，警告和其他提示信息
        private Messager messager;

        // 类名，如：MainActivity
        private ClassName className;

        // 方法参数体
        private ParameterSpec parameterSpec;

        public Builder(ParameterSpec parameterSpec) {
            this.parameterSpec = parameterSpec;
        }

        public Builder setMessager(Messager messager) {
            this.messager = messager;
            return this;
        }

        public Builder setClassName(ClassName className) {
            this.className = className;
            return this;
        }

        public EAutowiredFactory build() {
            if (parameterSpec == null) {
                throw new IllegalArgumentException("parameterSpec方法参数体为空");
            }

            if (className == null) {
                throw new IllegalArgumentException("方法内容中的className为空");
            }

            if (messager == null) {
                throw new IllegalArgumentException("messager为空，Messager用来报告错误、警告和其他提示信息");
            }

            return new EAutowiredFactory(this);
        }
    }
}
