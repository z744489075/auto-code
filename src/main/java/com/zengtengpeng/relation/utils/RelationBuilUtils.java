package com.zengtengpeng.relation.utils;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.bean.BuildXmlBean;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.enums.XmlElementType;
import com.zengtengpeng.relation.bean.RelationTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关系构建utils
 */
public class RelationBuilUtils {

    /**
     * 获取bean的java字段
     * @param relationTable
     * @param globalConfig
     * @return
     */
    public static List<BuildJavaField> getBaseBeanJavaFields(RelationTable relationTable, GlobalConfig globalConfig) {
        List<BuildJavaField> buildJavaFields=new ArrayList<>();
        BuildJavaField buildJavaField=new BuildJavaField();
        buildJavaField.setRemark(relationTable.getRemark());
        buildJavaField.setFiledName(relationTable.getBeanNameLower());
        buildJavaField.setReturnType(relationTable.getBeanName());
        if (globalConfig.getSwagger()){
            List<String> ans=new ArrayList<>();
            ans.add("@ApiModelProperty(hidden = true)");
            buildJavaField.setAnnotation(ans);
        }
        buildJavaField.setFiledType("private");
        buildJavaFields.add(buildJavaField);
        return buildJavaFields;
    }

    /**
     * 获取listbean字段
     * @param relationTable
     * @param globalConfig
     * @return
     */
    public static List<BuildJavaField> getBeanListJavaFields(RelationTable relationTable, GlobalConfig globalConfig) {
        List<BuildJavaField> buildJavaFields=new ArrayList<>();
        BuildJavaField buildJavaField=new BuildJavaField();
        buildJavaField.setRemark(relationTable.getRemark());
        buildJavaField.setFiledName(relationTable.getBeanNameLower()+"List");
        buildJavaField.setReturnType("List<"+relationTable.getBeanName()+">");
        buildJavaField.setFiledType("private");
        if (globalConfig.getSwagger()){
            List<String> ans=new ArrayList<>();
            ans.add("@ApiModelProperty(hidden = true)");
            buildJavaField.setAnnotation(ans);
        }
        buildJavaFields.add(buildJavaField);
        return buildJavaFields;
    }

    /**
     * 获取bean字段方法
     * @param relationTable
     * @return
     */
    public static List<BuildJavaMethod> getBaseBeanJavaMethods(RelationTable relationTable) {
        List<BuildJavaMethod> buildJavaMethods=new ArrayList<>();

        BuildJavaMethod get=new BuildJavaMethod();
        get.setContent(String.format("return %s;",relationTable.getBeanNameLower()));
        get.setRemark(relationTable.getRemark());
        get.setMethodName(String.format("get%s",relationTable.getBeanName()));
        get.setMethodType("public");
        get.setReturnType(relationTable.getBeanName());
        buildJavaMethods.add(get);

        BuildJavaMethod set=new BuildJavaMethod();
        set.setContent(String.format("this.%s = %s;",relationTable.getBeanNameLower(),relationTable.getBeanNameLower()));
        set.setRemark(relationTable.getRemark());
        set.setMethodName(String.format("set%s",relationTable.getBeanName()));
        set.setMethodType("public");
        set.setReturnType("void");
        List<String> param=new ArrayList<>();
        param.add(relationTable.getBeanName()+" "+relationTable.getBeanNameLower());
        set.setParams(param);
        buildJavaMethods.add(set);

        return buildJavaMethods;
    }

    /**
     * 获取list方法
     * @param relationTable
     * @return
     */
    public static List<BuildJavaMethod> getBeanListJavaMethods(RelationTable relationTable) {
        List<BuildJavaMethod> buildJavaMethods=new ArrayList<>();
        BuildJavaMethod get=new BuildJavaMethod();
        get.setContent(String.format("return %sList;",relationTable.getBeanNameLower()));
        get.setRemark(relationTable.getRemark());
        get.setMethodName(String.format("get%sList",relationTable.getBeanName()));
        get.setMethodType("public");
        get.setReturnType("List<"+relationTable.getBeanName()+">");
        buildJavaMethods.add(get);

        BuildJavaMethod set=new BuildJavaMethod();
        set.setContent(String.format("this.%sList = %sList;",relationTable.getBeanNameLower(),relationTable.getBeanNameLower()));
        set.setRemark(relationTable.getRemark());
        set.setMethodName(String.format("set%sList",relationTable.getBeanName()));
        set.setMethodType("public");
        set.setReturnType("void");
        List<String> param=new ArrayList<>();
        param.add(String.format("List<%s> %sList",relationTable.getBeanName(),relationTable.getBeanNameLower()) );
        set.setParams(param);
        buildJavaMethods.add(set);
        return buildJavaMethods;
    }

    /**
     * 获取级联删除
     * @param foreign
     * @param primary
     * @return
     */
    public static BuildXmlBean getXmlBaseDelete(RelationTable primary,RelationTable foreign) {
        BuildXmlBean buildXmlBean=new BuildXmlBean();
        buildXmlBean.setXmlElementType(XmlElementType.delete);
        Map<String, String> attrs=new HashMap<>();
        attrs.put("id",String.format("delete%sBy%s",foreign.getBeanName(),primary.getBeanName()));
        buildXmlBean.setAttributes(attrs);
        buildXmlBean.setSql(String.format("\t\tdelete from  %s\n" +
                "\t\twhere %s=#{%s}",foreign.getDataName(),foreign.getForeignKey(),foreign.getForeignKeyUp(false)));

        return buildXmlBean;
    }

    public static BuildJavaMethod getDaoBaseDelete(RelationTable primary, RelationTable foreign) {
        BuildJavaMethod buildJavaMethod=new BuildJavaMethod();
        buildJavaMethod.setRemark(String.format("根据%s删除%s",foreign.getRemark(),primary.getRemark()));
        buildJavaMethod.setReturnType("Integer");
        buildJavaMethod.setMethodName(String.format("delete%sBy%s",primary.getBeanName(),foreign.getBeanName()));
        List<String> params=new ArrayList<>();
        params.add(primary.getBeanName()+" "+primary.getBeanNameLower());
        buildJavaMethod.setParams(params);
        return buildJavaMethod;
    }
}
