package com.zengtengpeng.relation.config;

import com.zengtengpeng.autoCode.bean.BuildXmlBean;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.enums.XmlElementType;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 构建一对一Xml
 */
@FunctionalInterface
public interface BuildBaseXml {

    Logger logger = LoggerFactory.getLogger(BuildBaseXml.class);

    /**
     * 自定义构建
     * @param relationConfig 关系描述配置
     * @param primaryBuildXmlBean 主表sql
     * @param foreignBuildXmlBean 外表sql
     */
    void custom(RelationConfig relationConfig,List<BuildXmlBean> primaryBuildXmlBean,List<BuildXmlBean> foreignBuildXmlBean);


    /**
     * 外表级联删除(根据主键删除)
     * @return
     */
    default BuildXmlBean deleteByUserId(RelationConfig relationConfig){
        RelationTable foreign = relationConfig.getForeign();
        BuildXmlBean buildXmlBean=new BuildXmlBean();
        buildXmlBean.setXmlElementType(XmlElementType.delete);
        Map<String, String> attrs=new HashMap<>();
        attrs.put("id","deleteBy"+foreign.getForeignKeyUp(true));
        buildXmlBean.setAttributes(attrs);
        buildXmlBean.setSql(String.format("\t\tdelete from  %s\n" +
                "\t\twhere %s=#{%s}",foreign.getDataName(),foreign.getForeignKey(),foreign.getForeignKeyUp(false)));

        return buildXmlBean;
    }



    /**
     * 构建主表的dao
     */
    default void buildPrimary(RelationConfig relationConfig, List<BuildXmlBean> buildXmlBeans){
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        AutoCodeConfig autoCodeConfig = relationConfig.getAutoCodeConfig();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageXmlPath(globalConfig.getParentPathResources(),globalConfig.getXmlPath())+"/"+primary.getBeanName()+globalConfig.getPackageDaoUp()+".xml";
        BuildUtils.addXmlSql(filePath,buildXmlBeans);
    }
    /**
     * 构建外表的dao
     */
    default void buildForeign(RelationConfig relationConfig, List<BuildXmlBean> buildXmlBeans){
        RelationTable foreign = relationConfig.getForeign();
        AutoCodeConfig autoCodeConfig = relationConfig.getAutoCodeConfig();
        buildXmlBeans.add(deleteByUserId(relationConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageXmlPath(globalConfig.getParentPathResources(),globalConfig.getXmlPath())+"/"+foreign.getBeanName()+globalConfig.getPackageDaoUp()+".xml";
        BuildUtils.addXmlSql(filePath,buildXmlBeans);
    }


    /**
     * 开始构建
     */
    default void build(RelationConfig relationConfig){
        List<BuildXmlBean> p = new ArrayList<>();

        List<BuildXmlBean> f = new ArrayList<>();
        custom(relationConfig,p,f);
        buildPrimary(relationConfig,p);
        buildForeign(relationConfig,f);

    }
}
