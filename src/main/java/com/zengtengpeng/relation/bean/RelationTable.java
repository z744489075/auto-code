package com.zengtengpeng.relation.bean;

import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.jdbc.bean.Bean;

/**
 * 关系描述
 */
public class RelationTable {

    /**
     * test_code
     * 数据库表名称
     */
    private String dataName;


    /**
     * TestCode
     * java名称
     */
    private String beanName;


    /**
     * 备注
     */
    private String remark;

    /**
     * 是否需要生成单表
     */
    private Boolean generate=true;

    /**
     * 只有当 generate=false时候该参数生效
     * 如果不需要生成请填写父包   如 com.zengtengpeng.test.bean.TestCode  请填写 com.zengtengpeng.test
     */
    private String existParentPackage;

    /**
     * 外键id
     */
    private String foreignKey;
    /**
     * 主键id
     */
    private String primaryKey;


    public String getPrimaryKey() {
        return primaryKey;
    }
    public String getPrimaryKeyUp(Boolean flag) {
        return MyStringUtils.upperCase_(primaryKey,flag);
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getBeanName() {
        if(MyStringUtils.isEmpty(beanName)){
            return MyStringUtils.upperCase_(dataName,true);
        }
        return beanName;
    }
    public String getBeanNameLower() {
        if(!MyStringUtils.isEmpty(getBeanName())){
            return MyStringUtils.firstLowerCase(getBeanName());
        }
        return "";
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getRemark() {
        if(MyStringUtils.isEmpty(remark)){
            return dataName;
        }
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getGenerate() {
        return generate;
    }

    public void setGenerate(Boolean generate) {
        this.generate = generate;
    }

    public String getExistParentPackage() {
        return existParentPackage;
    }
    public String getExistParentPackage_() {
        if(!MyStringUtils.isEmpty(existParentPackage)){
            return existParentPackage.substring(existParentPackage.lastIndexOf(".")+1);
        }
        return "";
    }

    public void setExistParentPackage(String existParentPackage) {
        this.existParentPackage = existParentPackage;
    }

    public String getForeignKey() {
        return foreignKey;
    }
    public String getForeignKeyUp(Boolean flag) {
        return MyStringUtils.upperCase_(foreignKey,flag);
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }
}
