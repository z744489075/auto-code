package com.zengtengpeng.relation.ManyToMany;

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
import com.zengtengpeng.relation.utils.RelationBuilUtils;

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
    default BuildXmlBean primaryDelete(RelationConfig relationConfig){

        return RelationBuilUtils.getXmlBaseDelete(relationConfig.getForeign(),relationConfig.getPrimary());
    }
    /**
     * 主表级联查询
     * @return
     */
    default BuildXmlBean primarySelect(RelationConfig relationConfig){
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable thirdparty = relationConfig.getThirdparty();
        String primaryKey = thirdparty.getPrimaryKey();
        String primaryKeyUp = thirdparty.getPrimaryKeyUp(false);

        return select(relationConfig, primary, foreign, thirdparty, primaryKey, primaryKeyUp);
    }
    /**
     * 主表级联查询
     * @return
     */
    default BuildXmlBean foreignSelect(RelationConfig relationConfig){
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable thirdparty = relationConfig.getThirdparty();
        String foreignKey = thirdparty.getForeignKey();
        String foreignKeyUp = thirdparty.getForeignKeyUp(false);

        return select(relationConfig,  foreign,primary, thirdparty, foreignKey, foreignKeyUp);
    }

    /**
     * 多对多查询
     * @param relationConfig
     * @param primary
     * @param foreign
     * @param thirdparty
     * @param primaryKey
     * @param primaryKeyUp
     * @return
     */
    default BuildXmlBean select(RelationConfig relationConfig, RelationTable primary, RelationTable foreign, RelationTable thirdparty, String primaryKey, String primaryKeyUp) {
        AutoCodeConfig autoCodeConfig = relationConfig.getAutoCodeConfig();
        BuildXmlBean buildXmlBean=new BuildXmlBean();
        buildXmlBean.setXmlElementType(XmlElementType.select);
        Map<String, String> attrs=new HashMap<>();
        attrs.put("id",String.format("selecte%sBy%s",primary.getBeanName(),foreign.getBeanName()));
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
            MyStringUtils.append(content,"%s.%s,\n",3,primary.getDataName(),t.getJdbcName());
        });
        content.deleteCharAt(content.length()-2);
        MyStringUtils.append(content,"FROM",2);
        MyStringUtils.append(content,"%s,%s",3,primary.getDataName(),thirdparty.getDataName());
        MyStringUtils.append(content,"WHERE %s.%s=%s.%s",2,primary.getDataName(),primary.getPrimaryKey()
                ,thirdparty.getDataName(), primaryKey);
        MyStringUtils.append(content,"and %s.`%s`=#{%s}",2,thirdparty.getDataName(), primaryKey, primaryKeyUp);

        buildXmlBean.setSql(content.toString());
        return buildXmlBean;
    }


    @Override
    default void buildPrimary(RelationConfig relationConfig, List<BuildXmlBean> buildXmlBeans) {
        RelationTable primary = relationConfig.getPrimary();
        AutoCodeConfig autoCodeConfig = relationConfig.getAutoCodeConfig();
        buildXmlBeans.add(primarySelect(relationConfig));
        buildXmlBeans.add(primaryDelete(relationConfig));
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageXmlPath(globalConfig.getParentPathResources(),globalConfig.getXmlPath())+"/"+primary.getBeanName()+globalConfig.getPackageDaoUp()+".xml";
        BuildUtils.addXmlSql(filePath,buildXmlBeans);
    }

    @Override
    default void buildForeign(RelationConfig relationConfig, List<BuildXmlBean> buildXmlBeans) {
        RelationTable foreign = relationConfig.getForeign();
        AutoCodeConfig autoCodeConfig = relationConfig.getAutoCodeConfig();

        buildXmlBeans.add(foreignSelect(relationConfig));
        buildXmlBeans.add(foreignDelete(relationConfig));
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageXmlPath(globalConfig.getParentPathResources(),globalConfig.getXmlPath())+"/"+foreign.getBeanName()+globalConfig.getPackageDaoUp()+".xml";
        BuildUtils.addXmlSql(filePath,buildXmlBeans);
    }
}
