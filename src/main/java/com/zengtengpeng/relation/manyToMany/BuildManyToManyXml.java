package com.zengtengpeng.relation.manyToMany;

import com.zengtengpeng.autoCode.StartCode;
import com.zengtengpeng.autoCode.bean.BuildXmlBean;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.enums.XmlElementType;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.jdbc.bean.Bean;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.build.BuildBaseXml;
import com.zengtengpeng.relation.config.RelationConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 构建一对多Xml
 */
@FunctionalInterface
public interface BuildManyToManyXml extends BuildBaseXml {


    /**
     * 主表级联删除(根据主键删除)
     * @return
     */
    default BuildXmlBean primaryDeletePrimaryAndForeign(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable thirdparty = relationConfig.getThirdparty();

        BuildXmlBean buildXmlBean=new BuildXmlBean();
        buildXmlBean.setXmlElementType(XmlElementType.delete);
        Map<String, String> attrs=new HashMap<>();
        attrs.put("id",String.format("delete%sBy%s",primary.getBeanName(),foreign.getBeanName()));
        buildXmlBean.setAttributes(attrs);
        buildXmlBean.setSql(String.format("\t\tDELETE FROM %s WHERE %s=#{%s}",thirdparty.getDataName(),
                thirdparty.getForeignKey(),thirdparty.getForeignKeyUp(false)));

        return buildXmlBean;
    }

    @Override
    default BuildXmlBean foreignDeleteForeignByPrimary(AutoCodeConfig autoCodeConfig) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable thirdparty = relationConfig.getThirdparty();

        BuildXmlBean buildXmlBean=new BuildXmlBean();
        buildXmlBean.setXmlElementType(XmlElementType.delete);
        Map<String, String> attrs=new HashMap<>();
        attrs.put("id",String.format("delete%sBy%s",foreign.getBeanName(),primary.getBeanName()));
        buildXmlBean.setAttributes(attrs);
        buildXmlBean.setSql(String.format("\t\tDELETE FROM %s WHERE %s=#{%s}",thirdparty.getDataName(),thirdparty.getPrimaryKey(),thirdparty.getPrimaryKeyUp(false)));
        return buildXmlBean;
    }


    /**
     * 主表级联查询
     * @return
     */
    default BuildXmlBean primarySelectPrimaryByForeign(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable thirdparty = relationConfig.getThirdparty();

        BuildXmlBean buildXmlBean=new BuildXmlBean();
        buildXmlBean.setXmlElementType(XmlElementType.select);
        Map<String, String> attrs=new HashMap<>();
        attrs.put("id",String.format("select%sBy%s",primary.getBeanName(),foreign.getBeanName()));
        attrs.put("resultMap","BaseResultMap");
        buildXmlBean.setAttributes(attrs);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"SELECT",2);
        Bean bean=new Bean();
        bean.setDataName(primary.getDataName());
        bean.setTableName(primary.getBeanName());
        StartCode startCode= t->{};
        autoCodeConfig.setBean(bean);
        bean = startCode.saxTable(autoCodeConfig);

        bean.getAllColumns().forEach(t->{
            MyStringUtils.append(content,"%s.%s,",3,primary.getDataName(),t.getJdbcName());
        });
        content.deleteCharAt(content.length()-2);
        MyStringUtils.append(content,"FROM",2);
        MyStringUtils.append(content,"%s,%s",3,primary.getDataName(),thirdparty.getDataName());
        MyStringUtils.append(content,"WHERE %s.%s=%s.%s",2,primary.getDataName(),primary.getPrimaryKey()
                ,thirdparty.getDataName(), thirdparty.getPrimaryKey());
        MyStringUtils.append(content,"and %s.%s=#{%s}",2,thirdparty.getDataName(), thirdparty.getForeignKey(), thirdparty.getForeignKeyUp(false));

        BuildUtils.xmlWhere(bean, content);

        buildXmlBean.setSql(content.toString());
        return buildXmlBean;
    }
    /**
     * 外表级联查询
     * @return
     */
    default BuildXmlBean foreignSelectForeignByPrimary(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable thirdparty = relationConfig.getThirdparty();

        BuildXmlBean buildXmlBean=new BuildXmlBean();
        buildXmlBean.setXmlElementType(XmlElementType.select);
        Map<String, String> attrs=new HashMap<>();
        attrs.put("id",String.format("select%sBy%s",foreign.getBeanName(),primary.getBeanName()));
        attrs.put("resultMap","BaseResultMap");
        buildXmlBean.setAttributes(attrs);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"SELECT",2);
        Bean bean=new Bean();
        bean.setDataName(foreign.getDataName());
        bean.setTableName(foreign.getBeanName());
        StartCode startCode= t->{};
        autoCodeConfig.setBean(bean);
        bean = startCode.saxTable(autoCodeConfig);
        bean.getAllColumns().forEach(t->{
            MyStringUtils.append(content,"%s.%s,",3,foreign.getDataName(),t.getJdbcName());
        });
        content.deleteCharAt(content.length()-2);
        MyStringUtils.append(content,"FROM",2);
        MyStringUtils.append(content,"%s,%s",3,foreign.getDataName(),thirdparty.getDataName());
        MyStringUtils.append(content,"WHERE %s.%s=%s.%s",2,foreign.getDataName(),foreign.getForeignKey()
                ,thirdparty.getDataName(), thirdparty.getForeignKey());
        MyStringUtils.append(content,"and %s.%s=#{%s}",2,thirdparty.getDataName(), thirdparty.getPrimaryKey(), thirdparty.getPrimaryKeyUp(false));

        BuildUtils.xmlWhere(bean, content);

        buildXmlBean.setSql(content.toString());
        return buildXmlBean;
    }
    /**
     * 外表级联新增
     * @return
     */
    default BuildXmlBean foreignInsertRelation(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable thirdparty = relationConfig.getThirdparty();

        BuildXmlBean buildXmlBean=new BuildXmlBean();
        buildXmlBean.setXmlElementType(XmlElementType.insert);
        Map<String, String> attrs=new HashMap<>();
        attrs.put("id","insertRelation");
        buildXmlBean.setAttributes(attrs);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"INSERT INTO %s(",2,thirdparty.getDataName());
        MyStringUtils.append(content,"%s,%s",3,thirdparty.getPrimaryKey(),thirdparty.getForeignKey());
        MyStringUtils.append(content,") VALUES",2);
        MyStringUtils.append(content,"<foreach collection =\"%s\" item=\"row\"  separator =\",\">",2,thirdparty.getPrimaryKeyUp(false));
        MyStringUtils.append(content,"(",3);
        MyStringUtils.append(content,"#{row},#{%s}",3,foreign.getForeignKeyUp(false));
        MyStringUtils.append(content,")",3);
        MyStringUtils.append(content,"</foreach>",2);

        buildXmlBean.setSql(content.toString());
        return buildXmlBean;
    }
    /**
     * 主表表级联新增
     * @return
     */
    default BuildXmlBean primaryInsertRelation(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable thirdparty = relationConfig.getThirdparty();
        BuildXmlBean buildXmlBean=new BuildXmlBean();
        buildXmlBean.setXmlElementType(XmlElementType.insert);
        Map<String, String> attrs=new HashMap<>();
        attrs.put("id","insertRelation");
        buildXmlBean.setAttributes(attrs);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"INSERT INTO %s(",2,thirdparty.getDataName());
        MyStringUtils.append(content,"%s,%s",3,thirdparty.getPrimaryKey(),thirdparty.getForeignKey());
        MyStringUtils.append(content,") VALUES",2);
        MyStringUtils.append(content,"<foreach collection =\"%s\" item=\"row\"  separator =\",\">",2,thirdparty.getForeignKeyUp(false));
        MyStringUtils.append(content,"(",3);
        MyStringUtils.append(content,"#{%s},#{row}",3,foreign.getForeignKeyUp(false));
        MyStringUtils.append(content,")",3);
        MyStringUtils.append(content,"</foreach>",2);

        buildXmlBean.setSql(content.toString());
        return buildXmlBean;
    }




    @Override
    default void buildPrimary(AutoCodeConfig autoCodeConfig, List<BuildXmlBean> buildXmlBeans) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        buildXmlBeans.add(primarySelectPrimaryByForeign(autoCodeConfig));
        buildXmlBeans.add(primaryDeletePrimaryAndForeign(autoCodeConfig));
        buildXmlBeans.add(primaryInsertRelation(autoCodeConfig));
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageXmlPath(globalConfig.getParentPathResources(),globalConfig.getXmlPath())+"/"+primary.getBeanName()+globalConfig.getPackageDaoUp()+".xml";
        BuildUtils.addXmlSql(filePath,buildXmlBeans);
    }

    @Override
    default void buildForeign(AutoCodeConfig autoCodeConfig, List<BuildXmlBean> buildXmlBeans) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        buildXmlBeans.add(foreignSelectForeignByPrimary(autoCodeConfig));
        buildXmlBeans.add(foreignDeleteForeignByPrimary(autoCodeConfig));
        buildXmlBeans.add(foreignInsertRelation(autoCodeConfig));
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageXmlPath(globalConfig.getParentPathResources(),globalConfig.getXmlPath())+"/"+foreign.getBeanName()+globalConfig.getPackageDaoUp()+".xml";
        BuildUtils.addXmlSql(filePath,buildXmlBeans);
    }
}
