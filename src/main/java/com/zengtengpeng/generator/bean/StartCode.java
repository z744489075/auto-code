package com.zengtengpeng.generator.bean;

import java.util.List;

/**
 * 开始生成代码时需要的参数
 */
public class StartCode {

    private String parentPath; //生成代码的路径
    private String parentPack; //生产代码的父包
    private String jdbc; //jdbc连接
    private String user; //用户名
    private String password; //密码
    private List<String> dataNames; //表名

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getParentPack() {
        return parentPack;
    }

    public void setParentPack(String parentPack) {
        this.parentPack = parentPack;
    }

    public String getJdbc() {
        return jdbc;
    }

    public void setJdbc(String jdbc) {
        this.jdbc = jdbc;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getDataNames() {
        return dataNames;
    }

    public void setDataNames(List<String> dataNames) {
        this.dataNames = dataNames;
    }
}
