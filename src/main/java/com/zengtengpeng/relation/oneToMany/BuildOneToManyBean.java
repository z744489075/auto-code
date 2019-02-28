package com.zengtengpeng.relation.oneToMany;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.config.BuildBaseBean;
import com.zengtengpeng.relation.config.RelationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建一对多Bean
 */
@FunctionalInterface
public interface BuildOneToManyBean extends BuildBaseBean {

    /**
     * 主表导入
     * @return
     */
    default List<String> primaryImports(RelationConfig relationConfig){
        RelationTable foreign = relationConfig.getForeign();
        AutoCodeConfig autoCodeConfig = relationConfig.getAutoCodeConfig();
        List<String> im=new ArrayList<>();
        im.add(foreign.getExistParentPackage()+"."+autoCodeConfig.getGlobalConfig().getPackageBean()+"."+
                foreign.getBeanName());
        im.add("java.util.List");
        return im;
    }

    /**
     * 主表字段
     * @return
     */
    default List<BuildJavaField> primaryFields(RelationConfig relationConfig){
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        List<BuildJavaField> buildJavaFields=new ArrayList<>();
        BuildJavaField buildJavaField=new BuildJavaField();
        buildJavaField.setRemark(primary.getRemark());
        buildJavaField.setFiledName(foreign.getBeanNameLower()+"List");
        buildJavaField.setReturnType("List<"+foreign.getBeanName()+">");
        buildJavaField.setFiledType("private");
        buildJavaFields.add(buildJavaField);
        return buildJavaFields;
    }



    /**
     * 主表方法
     * @return
     */
    default List<BuildJavaMethod> primaryMethods(RelationConfig relationConfig){
        List<BuildJavaMethod> buildJavaMethods=new ArrayList<>();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod get=new BuildJavaMethod();
        get.setContent(String.format("return %sList;",foreign.getBeanNameLower()));
        get.setRemark(foreign.getRemark());
        get.setMethodName(String.format("get%sList",foreign.getBeanName()));
        get.setMethodType("public");
        get.setReturnType("List<"+foreign.getBeanName()+">");
        buildJavaMethods.add(get);

        BuildJavaMethod set=new BuildJavaMethod();
        set.setContent(String.format("this.%sList = %sList;",foreign.getBeanNameLower(),foreign.getBeanNameLower()));
        set.setRemark(foreign.getRemark());
        set.setMethodName(String.format("set%sList",foreign.getBeanName()));
        set.setMethodType("public");
        set.setReturnType("void");
        List<String> param=new ArrayList<>();
        param.add(String.format("List<%s> %sList",foreign.getBeanName(),foreign.getBeanNameLower()) );
        set.setParams(param);
        buildJavaMethods.add(set);

        return buildJavaMethods;
    }

}
