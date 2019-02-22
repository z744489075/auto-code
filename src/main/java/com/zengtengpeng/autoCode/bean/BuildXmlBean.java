package com.zengtengpeng.autoCode.bean;

import com.zengtengpeng.autoCode.enums.XmlElementType;

import java.util.Map;

public class BuildXmlBean {

    /**
     * xml的类型
     */
    private XmlElementType xmlElementType;

    /**
     * 属性
     */
    private Map<String,String> attributes;

    /**
     * sql 文本
     */
    private String sql;

    public XmlElementType getXmlElementType() {
        return xmlElementType;
    }

    public void setXmlElementType(XmlElementType xmlElementType) {
        this.xmlElementType = xmlElementType;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
