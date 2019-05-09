package com.zengtengpeng.autoCode.config;

import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.relation.config.RelationConfig;

import java.util.List;

public class GlobalConfig {

    /**
     * 表名称集合
     */
    private List<TableConfig> tableNames;

    /**
     * 表关系配置
     */
    private RelationConfig relationConfig;
    /**
     * 生成代码的父包 如父包是com.zengtengpeng.test  controller将在com.zengtengpeng.test.controller下 bean 将在com.zengtengpeng.test.bean下 ,service,dao同理
     */
    private String parentPack;
    /**
     * 生成代码的项目路径
     */
    private String parentPath;

    /**
     * 是否覆盖生成 默认不覆盖
     */
    private Boolean cover=false;

    private Boolean swagger=false;


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


    /**
     * 是否启用代码生成器
     */
    private Boolean autoCode=true;

    /**
     * 观察者模式.只能看.不能生成代码
     */
    private Boolean watchMobel=false;


    public Boolean getWatchMobel() {
        return watchMobel;
    }

    public void setWatchMobel(Boolean watchMobel) {
        this.watchMobel = watchMobel;
    }

    public Boolean getAutoCode() {
        return autoCode;
    }

    public void setAutoCode(Boolean autoCode) {
        this.autoCode = autoCode;
    }

    public Boolean getSwagger() {
        return swagger;
    }

    public void setSwagger(Boolean swagger) {
        this.swagger = swagger;
    }

    public Boolean getCover() {
        return cover;
    }

    public RelationConfig getRelationConfig() {
        return relationConfig;
    }

    public void setRelationConfig(RelationConfig relationConfig) {
        this.relationConfig = relationConfig;
    }

    public void setCover(Boolean cover) {
        this.cover = cover;
    }

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

    public String getPackageDaoUp() {
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
    public String getPackageControllerUp() {
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
    public String getPackageServiceUp() {
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
    public String getParentPathResources() {
        return parentPath+ "/" + getResources();
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getParentPack() {
        return parentPack;
    }

    public String getParentPathJavaSource() {
        return parentPath+ "/" + getJavaSource();
    }

    public void setParentPack(String parentPack) {
        this.parentPack = parentPack;
    }

}
