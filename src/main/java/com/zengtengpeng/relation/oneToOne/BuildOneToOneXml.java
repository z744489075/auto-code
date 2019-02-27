package com.zengtengpeng.relation.oneToOne;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.bean.BuildXmlBean;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
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
public interface BuildOneToOneXml {

    Logger logger = LoggerFactory.getLogger(BuildOneToOneXml.class);
    /**
     * 自定义构建
     * @param primary 主表自定义
     * @param foreign 外表自定义
     */
    void custom(List<BuildXmlBean> primary,List<BuildXmlBean> foreign);
    /**
     * 主表的配置
     * @return
     */
    default List<BuildXmlBean> customPrimary(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return new ArrayList<>();
    }
    /**
     * 外表的配置
     * @return
     */
    default List<BuildXmlBean> customForeign(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return new ArrayList<>();
    }


    /**
     * 外表级联删除(根据主键删除)
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     * @return
     */
    default BuildXmlBean deleteByUserId(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
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
     * @param primaryKey
     * @param autoCodeConfig
     */
    default void buildPrimary(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig,List<BuildXmlBean> buildXmlBeans){

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageXmlPath(globalConfig.getParentPathResources(),globalConfig.getXmlPath())+"/"+primaryKey.getBeanName()+globalConfig.getPackageDaoUp()+".xml";
        BuildUtils.addXmlSql(filePath,buildXmlBeans);
    }
    /**
     * 构建外表的dao
     * @param primaryKey
     * @param autoCodeConfig
     */
    default void buildForeign(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig,List<BuildXmlBean> buildXmlBeans){

        buildXmlBeans.add(deleteByUserId(primaryKey,foreign,autoCodeConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageXmlPath(globalConfig.getParentPathResources(),globalConfig.getXmlPath())+"/"+foreign.getBeanName()+globalConfig.getPackageDaoUp()+".xml";
        BuildUtils.addXmlSql(filePath,buildXmlBeans);
    }


    /**
     * 开始构建
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     */
    default void build(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        List<BuildXmlBean> p = customPrimary(primaryKey, foreign, autoCodeConfig);

        List<BuildXmlBean> f = customForeign(primaryKey, foreign, autoCodeConfig);
        custom(p,f);
        buildPrimary(primaryKey,foreign,autoCodeConfig,p);
        buildForeign(primaryKey,foreign,autoCodeConfig,f);

    }
}
