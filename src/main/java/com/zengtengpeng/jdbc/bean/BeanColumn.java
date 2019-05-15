package com.zengtengpeng.jdbc.bean;

import com.zengtengpeng.autoCode.utils.MyStringUtils;

import java.sql.JDBCType;

/**
 * 实体列
 */
public class BeanColumn {

    //数据库lie名
    private String jdbcName;

    //实体bean列名
    private String beanName;

    //jdbc类型
    private String jdbcType;
    //实体类型
    private String beanType;

    //是否为空
    private Boolean nullable = false;

    //数据库长度
    private String length;
    //是否自增
    private Boolean identity = false;

    //注释
    private String remarks;

    //默认值
    private String defaultValue;

    //是否是json
    private Boolean json=false;

    //是否是主键
    private Boolean key = false;


    public Boolean getJson() {
        return json;
    }

    public void setJson(Boolean json) {
        this.json = json;
    }

    public String getJdbcName() {
        return jdbcName;
    }

    public void setJdbcName(String jdbcName) {
        this.jdbcName = jdbcName;
    }

    public String getBeanName() {
        if (MyStringUtils.isEmpty(beanName)&& !MyStringUtils.isEmpty(jdbcName)) {
            return MyStringUtils.upperCase_(jdbcName, false);
        }
        return beanName;
    }

    public String getBeanName_() {
        if (MyStringUtils.isEmpty(beanName)&&!MyStringUtils.isEmpty(jdbcName)) {
            return MyStringUtils.upperCase_(jdbcName, true);
        }
        if(!MyStringUtils.isEmpty(beanName)){
            return MyStringUtils.firstUpperCase(beanName);
        }
        return beanName;
    }


    public String getJdbcType() {
        return jdbcType;
    }

    public String getJdbcType_() {
        return JDBCType.valueOf(Integer.valueOf(jdbcType)).name();
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setBeanType(String beanType) {
        this.beanType = beanType;
    }

    public String getBeanType() {

        if (MyStringUtils.isEmpty(beanType)&&!MyStringUtils.isEmpty(jdbcType)) {
            switch (JDBCType.valueOf(Integer.valueOf(jdbcType))) {
                case BIT:
                    return "java.lang.Byte";
                case TINYINT:
                case SMALLINT:
                case INTEGER:
                    return "java.lang.Integer";
                case BIGINT:
                    return "java.lang.Long";
                case FLOAT:
                    return "java.lang.Float";
                case REAL:
                case DOUBLE:
                case NUMERIC:
                case DECIMAL:
                    return "java.math.BigDecimal";
                case CHAR:
                    return "java.lang.Character";
                case VARCHAR:
                case LONGVARCHAR:
                    return "java.lang.String";
                case DATE:
                case TIME:
                case TIMESTAMP:
                    return "java.util.Date";
                default:
                    return "java.lang.String";
            }
        }
        return beanType;
    }

    public String getBeanType_() {
        String beanType = getBeanType();
        if (!MyStringUtils.isEmpty(beanType)) {
            String[] split = beanType.split("\\.");
            return split[split.length-1];
        }
        return "";
    }


    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Boolean getIdentity() {
        return identity;
    }

    public void setIdentity(Boolean identity) {
        this.identity = identity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getKey() {
        return key;
    }

    public void setKey(Boolean key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "BeanColumn{" +
                "jdbcName='" + jdbcName + '\'' +
                ", beanName='" + getBeanName() + '\'' +
                ", jdbcType='" + jdbcType + '\'' +
                ", beanType='" + beanType + '\'' +
                ", nullable=" + nullable +
                ", length='" + length + '\'' +
                ", identity=" + identity +
                ", remarks='" + remarks + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", key=" + key +
                '}';
    }
}
