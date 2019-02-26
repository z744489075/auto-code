package com.zengtengpeng.autoCode.bean;

import java.util.ArrayList;
import java.util.List;

public class BuildJavaMethod {
    /**
     * 注解
     */
    private List<String> annotation;

    /**
     * 返回值类型
     */
    private String returnType;

    /**
     * 方法类型  如: public List<SysAuth> queryByRole(SysRole sysRole) 中的 public
     */
    private String methodType="public";

    /**
     * 参数,已文本串的形式书写.一个参数就是一个String List<String> auths, @Param("roleId") Integer roleId
     */
    private List<String> params;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 具体代码
     */
    private String content;

    private String remark;


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
