package com.zengtengpeng.relation.manyToMany;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.build.BuildBaseBean;
import com.zengtengpeng.relation.config.RelationConfig;
import com.zengtengpeng.relation.utils.RelationBuilUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建多对多Bean
 */
@FunctionalInterface
public interface BuildManyToManyBean extends BuildBaseBean {


    /**
     * 主表字段
     * @return
     */
    default List<BuildJavaField> primaryFields(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        List<BuildJavaField> beanListJavaFields = RelationBuilUtils.getBeanListJavaFields(foreign, autoCodeConfig.getGlobalConfig());
        RelationTable thirdparty = relationConfig.getThirdparty();

        BuildJavaField buildJavaField=new BuildJavaField();
        buildJavaField.setRemark(foreign.getRemark()+"id");
        buildJavaField.setFiledName(thirdparty.getForeignKeyUp(false));
        buildJavaField.setReturnType("String");
        buildJavaField.setFiledType("private");
        if (autoCodeConfig.getGlobalConfig().getSwagger()){
            List<String> ans=new ArrayList<>();
            ans.add("@ApiModelProperty(value = \""+buildJavaField.getRemark()+"\")");
            buildJavaField.setAnnotation(ans);
        }
        beanListJavaFields.add(buildJavaField);

        return beanListJavaFields;
    }

    /**
     * 主表方法
     * @return
     */
    default List<BuildJavaMethod> primaryMethods(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        List<BuildJavaMethod> beanListJavaMethods = RelationBuilUtils.getBeanListJavaMethods(foreign);
        RelationTable thirdparty = relationConfig.getThirdparty();
        //set
        BuildJavaMethod set = new BuildJavaMethod();
        set.setReturnType("void");
        set.setMethodType("public");
        set.setMethodName("set"+thirdparty.getForeignKeyUp(true));
        List<String> params=new ArrayList<>();
        params.add(String.format("String %s",thirdparty.getForeignKeyUp(false)));
        set.setParams(params);
        set.setContent(String.format("this.%s=%s;",thirdparty.getForeignKeyUp(false),thirdparty.getForeignKeyUp(false)));
        beanListJavaMethods.add(set);

        BuildJavaMethod get = new BuildJavaMethod();
        get.setReturnType("String");
        get.setMethodType("public");
        get.setMethodName("get"+thirdparty.getForeignKeyUp(true));
        get.setContent("return "+thirdparty.getForeignKeyUp(false)+";");
        beanListJavaMethods.add(get);
        return beanListJavaMethods;
    }


    @Override
    default List<BuildJavaField> foreignFields(AutoCodeConfig autoCodeConfig) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        List<BuildJavaField> beanListJavaFields = RelationBuilUtils.getBeanListJavaFields(primary, autoCodeConfig.getGlobalConfig());
        RelationTable thirdparty = relationConfig.getThirdparty();

        BuildJavaField buildJavaField=new BuildJavaField();
        buildJavaField.setRemark(primary.getRemark()+"id");
        buildJavaField.setFiledName(thirdparty.getPrimaryKeyUp(false));
        buildJavaField.setReturnType("String");
        buildJavaField.setFiledType("private");
        if (autoCodeConfig.getGlobalConfig().getSwagger()){
            List<String> ans=new ArrayList<>();
            ans.add("@ApiModelProperty(value = \""+buildJavaField.getRemark()+"\")");
            buildJavaField.setAnnotation(ans);
        }
        beanListJavaFields.add(buildJavaField);
        return beanListJavaFields;
    }

    @Override
    default List<BuildJavaMethod> foreignMethods(AutoCodeConfig autoCodeConfig) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        List<BuildJavaMethod> beanListJavaMethods = RelationBuilUtils.getBeanListJavaMethods(primary);
        RelationTable thirdparty = relationConfig.getThirdparty();
        //set
        BuildJavaMethod set = new BuildJavaMethod();
        set.setReturnType("void");
        set.setMethodType("public");
        set.setMethodName("set"+thirdparty.getPrimaryKeyUp(true));
        List<String> params=new ArrayList<>();
        params.add(String.format("String %s",thirdparty.getPrimaryKeyUp(false)));
        set.setParams(params);
        set.setContent(String.format("this.%s=%s;",thirdparty.getPrimaryKeyUp(false),thirdparty.getPrimaryKeyUp(false)));
        beanListJavaMethods.add(set);

        BuildJavaMethod get = new BuildJavaMethod();
        get.setReturnType("String");
        get.setMethodType("public");
        get.setMethodName("get"+thirdparty.getPrimaryKeyUp(true));
        get.setContent("return "+thirdparty.getPrimaryKeyUp(false)+";");
        beanListJavaMethods.add(get);
        return beanListJavaMethods;
    }
}
