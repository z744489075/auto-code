package com.zengtengpeng.relation.build;

import com.zengtengpeng.autoCode.bean.BuildXmlBean;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.config.RelationConfig;
import com.zengtengpeng.relation.utils.RelationBuilUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建一对一Xml
 */
@FunctionalInterface
public interface BuildBaseXml {

    Logger logger = LoggerFactory.getLogger(BuildBaseXml.class);

    /**
     * 自定义构建
     * @param autoCodeConfig 关系描述配置
     * @param primaryBuildXmlBean 主表sql
     * @param foreignBuildXmlBean 外表sql
     */
    void custom(AutoCodeConfig autoCodeConfig, List<BuildXmlBean> primaryBuildXmlBean, List<BuildXmlBean> foreignBuildXmlBean);



    /**
     * 外表级联删除(根据主键删除)
     * @return
     */
    default BuildXmlBean foreignDeleteForeignByPrimary(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        return RelationBuilUtils.getXmlBaseDelete(relationConfig.getPrimary(),relationConfig.getForeign());
    }


    /**
     * 构建主表的dao
     */
    default void buildPrimary(AutoCodeConfig autoCodeConfig, List<BuildXmlBean> buildXmlBeans){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageXmlPath(globalConfig.getParentPathResources(),globalConfig.getXmlPath())+"/"+primary.getBeanName()+globalConfig.getPackageDaoUp()+".xml";
        BuildUtils.addXmlSql(filePath,buildXmlBeans);
    }
    /**
     * 构建外表的dao
     */
    default void buildForeign(AutoCodeConfig autoCodeConfig, List<BuildXmlBean> buildXmlBeans){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        buildXmlBeans.add(foreignDeleteForeignByPrimary(autoCodeConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageXmlPath(globalConfig.getParentPathResources(),globalConfig.getXmlPath())+"/"+foreign.getBeanName()+globalConfig.getPackageDaoUp()+".xml";
        BuildUtils.addXmlSql(filePath,buildXmlBeans);
    }


    /**
     * 开始构建
     */
    default void build(AutoCodeConfig autoCodeConfig){
        List<BuildXmlBean> p = new ArrayList<>();

        List<BuildXmlBean> f = new ArrayList<>();
        custom(autoCodeConfig,p,f);
        buildPrimary(autoCodeConfig,p);
        buildForeign(autoCodeConfig,f);

    }
}
