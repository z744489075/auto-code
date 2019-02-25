package com.zengtengpeng.autoCode.bean;

import java.util.List;

/**
 * 构建java字段
 */
public class BuildJavaField {

    /**
     * 注解
     */
    private List<String> annotation;

    /**
     * 返回值类型
     */
    private String returnType;
    /**
     * 字段类型类型  如 private String fileType; 中的 private
     */
    private String fileType;

    /**
     * 字段名
     */
    private String filedName;

    /**
     * 初始化字段  private List<String> annotation=new ArrayList<>(); 中的 new ArrayList<>()
     */
    private String init;

    public List<String> getAnnotation() {
        return annotation;
    }

    public void setAnnotation(List<String> annotation) {
        this.annotation = annotation;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }
}
