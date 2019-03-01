package com.zengtengpeng.relation.build;

import com.zengtengpeng.autoCode.bean.BuildXmlBean;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.enums.XmlElementType;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.config.RelationConfig;
import com.zengtengpeng.relation.utils.RelationBuilUtils;
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
    void custom(RelationConfig relationConfig, List<BuildXmlBean> primaryBuildXmlBean, List<BuildXmlBean> foreignBuildXmlBean);



    /**
     * 外表级联删除(根据主键删除)
     * @return
     */
    default BuildXmlBean foreignDelete(RelationConfig relationConfig){

        return RelationBuilUtils.getXmlBaseDelete(relationConfig.getPrimary(),relationConfig.getForeign());
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
        buildXmlBeans.add(foreignDelete(relationConfig));

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
