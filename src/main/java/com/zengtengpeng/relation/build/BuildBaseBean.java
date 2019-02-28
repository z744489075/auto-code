package com.zengtengpeng.relation.build;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.config.RelationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建一对一Bean
 */
@FunctionalInterface
public interface BuildBaseBean {

    Logger logger = LoggerFactory.getLogger(BuildBaseBean.class);

    /**
     * 自定义构建
     * @param relationConfig 关系描述配置
     * @param primaryBuildJavaConfig 主表自定义配置
     * @param foreignBuildJavaConfig 外表自定义配置
     */
    void custom(RelationConfig relationConfig, BuildJavaConfig primaryBuildJavaConfig, BuildJavaConfig foreignBuildJavaConfig);

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
        return im;
    }

    /**
     * 主表字段
     * @return
     */
    default List<BuildJavaField> primaryFields(RelationConfig relationConfig){
        return getBuildJavaFields(relationConfig.getForeign());
    }

    /**
     * 主表方法
     * @return
     */
    default List<BuildJavaMethod> primaryMethods(RelationConfig relationConfig){
        return getBuildJavaMethods(relationConfig.getForeign());
    }

    default List<BuildJavaMethod> getBuildJavaMethods(RelationTable relationTable) {
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
     * 外表导入
     * @return
     */
    default List<String> foreignImports(RelationConfig relationConfig){
        RelationTable primary = relationConfig.getPrimary();
        AutoCodeConfig autoCodeConfig = relationConfig.getAutoCodeConfig();
        List<String> im=new ArrayList<>();
        im.add(primary.getExistParentPackage()+"."+autoCodeConfig.getGlobalConfig().getPackageBean()+"."+
                primary.getBeanName());
        return im;
    }
    

    /**
     * 外表字段
     * @return
     */
    default List<BuildJavaField> foreignFields(RelationConfig relationConfig){
        return getBuildJavaFields(relationConfig.getPrimary());
    }

    default List<BuildJavaField> getBuildJavaFields(RelationTable relationTable) {
        List<BuildJavaField> buildJavaFields=new ArrayList<>();
        BuildJavaField buildJavaField=new BuildJavaField();
        buildJavaField.setRemark(relationTable.getRemark());
        buildJavaField.setFiledName(relationTable.getBeanNameLower());
        buildJavaField.setReturnType(relationTable.getBeanName());
        buildJavaField.setFiledType("private");
        buildJavaFields.add(buildJavaField);
        return buildJavaFields;
    }

    /**
     * 外表方法
     * @return
     */
    default List<BuildJavaMethod> foreignMethods(RelationConfig relationConfig){
        return getBuildJavaMethods(relationConfig.getPrimary());
    }


    /**
     * 构建主表的bean
     * @param buildJavaConfig
     */
    default void buildPrimary(RelationConfig relationConfig, BuildJavaConfig buildJavaConfig){
        RelationTable primary = relationConfig.getPrimary();
        AutoCodeConfig autoCodeConfig = relationConfig.getAutoCodeConfig();
        List<BuildJavaMethod> buildJavaMethods =buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.addAll(primaryMethods(relationConfig));

        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(primaryImports(relationConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        buildJavaFields.addAll(primaryFields(relationConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), primary.getExistParentPackage(),
                globalConfig.getPackageBean())+"/"+primary.getBeanName()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }
    /**
     * 构建外表的bean
     */
    default void buildForeign(RelationConfig relationConfig, BuildJavaConfig buildJavaConfig){
        RelationTable foreign = relationConfig.getForeign();
        AutoCodeConfig autoCodeConfig = relationConfig.getAutoCodeConfig();
        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.addAll(foreignMethods(relationConfig));

        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(foreignImports(relationConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        buildJavaFields.addAll(foreignFields(relationConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), foreign.getExistParentPackage(),
                globalConfig.getPackageBean())+"/"+foreign.getBeanName()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }


    /**
     * 开始构建
     */
    default void build(RelationConfig relationConfig){

        BuildJavaConfig p = new BuildJavaConfig();
        BuildJavaConfig f = new BuildJavaConfig();
        custom(relationConfig,p,f);
        //获取受影响的文件 controller,server,serverImpl dao xml
        buildPrimary(relationConfig,p);
        buildForeign(relationConfig,f);

    }
}
