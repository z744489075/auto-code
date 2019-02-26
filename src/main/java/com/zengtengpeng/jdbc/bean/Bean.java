package com.zengtengpeng.jdbc.bean;

import com.zengtengpeng.autoCode.utils.MyStringUtils;

import java.util.List;
import java.util.Map;

/**
 * 实体类
 */
public class Bean {

    /**
     * 主键
     */
    private List<BeanColumn> primaryKey;

    /**
     * 常量字段 在数据库注释使用json描述
     */
    private Map<Object,Object> cons;

    /**
     * 表注释
     */
    private String tableRemarks;

    /**
     * 所有的列
     */
    private List<BeanColumn> allColumns;

    /**
     * 父包路径
     */
    private String parentPack;
    /**
     * 模块名称  com.zengtengpeng.bean  com.zengtengpeng.controller 中的 zengtengpeng
     */
    private String mobelName;

    /**
     * javabean 名称 AutoCode
     */
    private String tableName;

    /**
     * javaBean 首字母小写 autoCode
     */
    private String tableValue;

    /**
     * 数据库名  auto_code
     */
    private String dataName;


    public List<BeanColumn> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<BeanColumn> primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Map<Object, Object> getCons() {
        return cons;
    }

    public void setCons(Map<Object, Object> cons) {
        this.cons = cons;
    }

    public String getTableRemarks() {
        return tableRemarks;
    }

    public void setTableRemarks(String tableRemarks) {
        this.tableRemarks = tableRemarks;
    }

    public List<BeanColumn> getAllColumns() {
        return allColumns;
    }

    public void setAllColumns(List<BeanColumn> allColumns) {
        this.allColumns = allColumns;
    }

    public String getParentPack() {
        return parentPack;
    }

    public void setParentPack(String parentPack) {
        this.parentPack = parentPack;
    }

    public String getMobelName() {
        if(MyStringUtils.isEmpty(mobelName)){
            return parentPack.substring(parentPack.lastIndexOf(".")+1);
        }
        return mobelName;
    }

    public void setMobelName(String mobelName) {
        this.mobelName = mobelName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableValue() {
        if(MyStringUtils.isEmpty(tableValue)){
            return MyStringUtils.firstLowerCase(tableName);
        }
        return tableValue;
    }

    public void setTableValue(String tableValue) {
        this.tableValue = tableValue;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
}
