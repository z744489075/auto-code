package com.zengtengpeng.autoCode.config;


import com.zengtengpeng.jdbc.bean.Bean;

/**
 * 全局配置
 */
public class AutoCodeConfig {
    //数据库配置
    private DatasourceConfig datasourceConfig;

    //全局配置
    private GlobalConfig globalConfig;

    private Bean bean;

    public DatasourceConfig getDatasourceConfig() {
        return datasourceConfig;
    }

    public void setDatasourceConfig(DatasourceConfig datasourceConfig) {
        this.datasourceConfig = datasourceConfig;
    }

    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public void setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    public Bean getBean() {
        return bean;
    }

    public void setBean(Bean bean) {
        this.bean = bean;
    }
}
