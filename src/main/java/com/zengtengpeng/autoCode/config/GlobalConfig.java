package com.zengtengpeng.autoCode.config;


import com.zengtengpeng.autoCode.create.BuildDao;
import com.zengtengpeng.autoCode.create.BuildXml;
import com.zengtengpeng.autoCode.utils.MyStringUtils;

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


    /**
     * java在项目中的classpath位置
     */
    private String javaSource="src/main/java";

    /**
     * resource位置
     */
    private String resources="src/main/resources";
    /**
     * xml存放的路径
     */
    private String xmlPath="mybatisMapper";

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

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public String getJavaSource() {
        return javaSource;
    }

    public void setJavaSource(String javaSource) {
        this.javaSource = javaSource;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
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

    public String getPackageDao_() {
        if(!MyStringUtils.isEmpty(packageDao)){
            return MyStringUtils.firstUpperCase(packageDao);
        }
        return "";
    }

    public void setPackageDao(String packageDao) {
        this.packageDao = packageDao;
    }

    public String getPackageController() {
        return packageController;
    }
    public String getPackageController_() {
        if(!MyStringUtils.isEmpty(packageController)){
            return MyStringUtils.firstUpperCase(packageController);
        }
        return "";
    }

    public void setPackageController(String packageController) {
        this.packageController = packageController;
    }

    public String getPackageService() {
        return packageService;
    }
    public String getPackageService_() {
        if(!MyStringUtils.isEmpty(packageService)){
            return MyStringUtils.firstUpperCase(packageService);
        }
        return "";
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
