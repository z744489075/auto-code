package com.zengtengpeng.autoCode.config;

import javax.sql.DataSource;

/**
 * 数据库配置
 */
public class DatasourceConfig {

    //驱动名称
    private String driverClassName;

    //数据库名称
    private String name;
    //jdbc链接
    private String url;

    //数据库用户名
    private String username;

    //数据库密码
    private String password;

    private DataSource dataSource;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
