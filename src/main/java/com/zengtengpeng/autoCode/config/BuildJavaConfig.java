package com.zengtengpeng.autoCode.config;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建java文件需要的配置
 */
public class BuildJavaConfig {

    /**
     * 需要导入的类
     */
    private List<String> imports=new ArrayList<>();

    /**
     * 需要继承的类(如果是接口则有多个)
     */
    private List<String> extend;

    /**
     *  需要实现的接口
     */
    private List<String> implement=new ArrayList<>();

    /**
     * 注解 带上@符号
     */
    private List<String> annotations;

    /**
     * 注释
     */
    private String remark;

    /**
     * 是否使用默认的一些导入类,以及继承,实现
     */
    private Boolean defaultRealize=true;

    /**
     * 方法
     */
    private List<BuildJavaMethod> buildJavaMethods=new ArrayList<>();

    /**
     * 字段
     */
    private List<BuildJavaField> buildJavaFields=new ArrayList<>();

    /**
     * 拼装好之后的内容
     */
    private StringBuffer content=new StringBuffer();


    public StringBuffer getContent() {
        return content;
    }

    public void setContent(StringBuffer content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getDefaultRealize() {
        return defaultRealize;
    }

    public void setDefaultRealize(Boolean defaultRealize) {
        this.defaultRealize = defaultRealize;
    }

    public List<BuildJavaMethod> getBuildJavaMethods() {
        return buildJavaMethods;
    }

    public void setBuildJavaMethods(List<BuildJavaMethod> buildJavaMethods) {
        this.buildJavaMethods = buildJavaMethods;
    }

    public List<BuildJavaField> getBuildJavaFields() {
        return buildJavaFields;
    }

    public void setBuildJavaFields(List<BuildJavaField> buildJavaFields) {
        this.buildJavaFields = buildJavaFields;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public List<String> getExtend() {
        return extend;
    }

    public void setExtend(List<String> extend) {
        this.extend = extend;
    }

    public List<String> getImplement() {
        return implement;
    }

    public void setImplement(List<String> implement) {
        this.implement = implement;
    }
}
