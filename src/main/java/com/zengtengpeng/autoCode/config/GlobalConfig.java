package com.zengtengpeng.autoCode.config;


import com.zengtengpeng.autoCode.create.BuildDao;
import com.zengtengpeng.autoCode.create.BuildXml;

import java.util.List;

public class GlobalConfig {

    /**
     * 表名称集合
     */
    private List<TableConfig> tableNames;
    /**
     * 生成代码的项目路径
     */
    private String parentPath;
    /**
     * 生成代码的父包 如父包是com.zengtengpeng.test  controller将在com.zengtengpeng.test.controller下 bean 将在com.zengtengpeng.test.bean下 ,service,dao同理
     */
    private String parentPack;

    private BuildXml buildXml;

    private BuildDao buildDao;

    /**
     * bean的名称
     */
    private String packageBean="bean";
    /**
     * dao的名称
     */
    private String packageDao="dao";
    /**
     * controller的名称
     */
    private String packageController="controller";
    /**
     * service的名称
     */
    private String packageService="service";


    public BuildXml getBuildXml() {
        return buildXml;
    }

    public void setBuildXml(BuildXml buildXml) {
        this.buildXml = buildXml;
    }

    public BuildDao getBuildDao() {
        return buildDao;
    }

    public void setBuildDao(BuildDao buildDao) {
        this.buildDao = buildDao;
    }

    public String getPackageBean() {
        return packageBean;
    }

    public void setPackageBean(String packageBean) {
        this.packageBean = packageBean;
    }

    public String getPackageDao() {
        return packageDao;
    }

    public void setPackageDao(String packageDao) {
        this.packageDao = packageDao;
    }

    public String getPackageController() {
        return packageController;
    }

    public void setPackageController(String packageController) {
        this.packageController = packageController;
    }

    public String getPackageService() {
        return packageService;
    }

    public void setPackageService(String packageService) {
        this.packageService = packageService;
    }

    public List<TableConfig> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<TableConfig> tableNames) {
        this.tableNames = tableNames;
    }

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

}
