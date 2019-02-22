package com.zengtengpeng.autoCode.config;


import com.zengtengpeng.autoCode.create.BuildDao;
import com.zengtengpeng.autoCode.create.BuildService;
import com.zengtengpeng.autoCode.create.BuildXml;
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

    private BuildJavaConfig buildJavaConfig;


    private BuildDao buildDao=t->null;
    private BuildService buildService=t->null;

    private BuildXml buildXml=t->null;

    public BuildService getBuildService() {
        return buildService;
    }

    public void setBuildService(BuildService buildService) {
        this.buildService = buildService;
    }

    public BuildDao getBuildDao() {
        return buildDao;
    }

    public void setBuildDao(BuildDao buildDao) {
        this.buildDao = buildDao;
    }

    public BuildXml getBuildXml() {
        return buildXml;
    }

    public void setBuildXml(BuildXml buildXml) {
        this.buildXml = buildXml;
    }

    public Bean getBean() {
        return bean;
    }

    public void setBean(Bean bean) {
        this.bean = bean;
    }

    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public void setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    public DatasourceConfig getDatasourceConfig() {
        return datasourceConfig;
    }

    public void setDatasourceConfig(DatasourceConfig datasourceConfig) {
        this.datasourceConfig = datasourceConfig;
    }

    public BuildJavaConfig getBuildJavaConfig() {
        return buildJavaConfig;
    }

    public void setBuildJavaConfig(BuildJavaConfig buildJavaConfig) {
        this.buildJavaConfig = buildJavaConfig;
    }
}
