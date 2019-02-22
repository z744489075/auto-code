package com.zengtengpeng.autoCode.config;

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
