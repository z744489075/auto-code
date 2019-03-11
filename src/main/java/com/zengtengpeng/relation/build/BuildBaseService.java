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
 * 构建一对一service
 */
@FunctionalInterface
public interface BuildBaseService {

    Logger logger = LoggerFactory.getLogger(BuildBaseService.class);
    /**
     * 自定义构建
     * @param autoCodeConfig 关系描述配置
     * @param primaryBuildJavaConfig 主表自定义配置
     * @param foreignBuildJavaConfig 外表自定义配置
     */
    void custom(AutoCodeConfig autoCodeConfig, BuildJavaConfig primaryBuildJavaConfig, BuildJavaConfig foreignBuildJavaConfig);



    /**
     * 构建外表 级联查询(带分页)
     * @return
     */
    default BuildJavaMethod foreignSelectPrimaryAndForeign(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        foreignSelect.setReturnType(foreignBeanName);
        foreignSelect.setMethodName(String.format("select%sAnd%s", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(foreignBeanName +" "+ foreign.getBeanNameLower());
        foreignSelect.setParams(params);
        foreignSelect.setRemark("级联查询(带分页) "+primary.getRemark()+"--"+foreign.getRemark());
        return foreignSelect;
    }
    /**
     * 构建外表 级联查询
     * @return
     */
    default BuildJavaMethod foreignSelectPrimaryAndForeignByCondition(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        foreignSelect.setReturnType(String.format("List<%s>",foreignBeanName));
        foreignSelect.setMethodName(String.format("select%sAnd%sByCondition", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(foreignBeanName +" "+ foreign.getBeanNameLower());
        foreignSelect.setParams(params);
        foreignSelect.setRemark("级联条件查询 "+primary.getRemark()+"--"+foreign.getRemark());
        return foreignSelect;
    }



    /**
     * 构建主表 级联查询(带分页)
     * @return
     */
    default BuildJavaMethod primarySelectPrimaryAndForeign(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        foreignSelect.setReturnType(primaryKeyBeanName);
        foreignSelect.setMethodName(String.format("select%sAnd%s", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(primary.getBeanName() +" "+ primary.getBeanNameLower());
        foreignSelect.setParams(params);
        foreignSelect.setRemark("级联查询(带分页) "+primary.getRemark()+"--"+foreign.getRemark());
        return foreignSelect;
    }
    /**
     * 构建主表 级联条件查询
     * @return
     */
    default BuildJavaMethod primarySelectPrimaryAndForeignByCondition(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        foreignSelect.setReturnType(String.format("List<%s>",primaryKeyBeanName));
        foreignSelect.setMethodName(String.format("select%sAnd%sByCondition", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(primary.getBeanName() +" "+ primary.getBeanNameLower());
        foreignSelect.setParams(params);
        foreignSelect.setRemark("级联查询(带分页) "+primary.getRemark()+"--"+foreign.getRemark());
        return foreignSelect;
    }

    /**
     * 主表级联删除(根据主键删除)
     * @return
     */
    default BuildJavaMethod primaryDeletePrimaryAndForeign(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        String primaryKeyBeanName_ = primary.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        BuildJavaMethod deleteTestUserAndTestClass = new BuildJavaMethod();
        deleteTestUserAndTestClass.setReturnType("Integer");
        deleteTestUserAndTestClass.setMethodName(String.format("delete%sAnd%s", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(primaryKeyBeanName +" "+ primaryKeyBeanName_);
        deleteTestUserAndTestClass.setParams(params);
        deleteTestUserAndTestClass.setRemark("级联删除(根据主键删除) "+primary.getRemark()+"--"+foreign.getRemark());
        return deleteTestUserAndTestClass;
    }
    /**
     * 外表级联删除(根据主键删除)
     * @return
     */
    default BuildJavaMethod foreignDelete(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        BuildJavaMethod deleteTestUserAndTestClass = new BuildJavaMethod();
        deleteTestUserAndTestClass.setReturnType("Integer");
        deleteTestUserAndTestClass.setMethodName(String.format("delete%sAnd%s", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(foreign.getBeanName() +" "+ foreign.getBeanNameLower());
        deleteTestUserAndTestClass.setParams(params);
        deleteTestUserAndTestClass.setRemark("级联删除(根据主键删除) "+primary.getRemark()+"--"+foreign.getRemark());
        return deleteTestUserAndTestClass;
    }

    /**
     * 外表导入
     * @return
     */
    default List<String> foreignImports(AutoCodeConfig autoCodeConfig){
        List<String> im=new ArrayList<>();
        im.add("java.util.List");
        return im;
    }
    /**
     * 主表导入
     * @return
     */
    default List<String> primaryImports(AutoCodeConfig autoCodeConfig){
        List<String> im=new ArrayList<>();
        im.add("java.util.List");
        return im;
    }

    /**
     * 构建主表的service
     */
    default void buildPrimary(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        List<BuildJavaMethod> buildJavaMethods =buildJavaConfig.getBuildJavaMethods();
        RelationTable primary = relationConfig.getPrimary();
        buildJavaMethods.add(primarySelectPrimaryAndForeign(autoCodeConfig));
        buildJavaMethods.add(primarySelectPrimaryAndForeignByCondition(autoCodeConfig));
        buildJavaMethods.add(primaryDeletePrimaryAndForeign(autoCodeConfig));

        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(primaryImports(autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), primary.getExistParentPackage(),
                globalConfig.getPackageService())+"/"+primary.getBeanName()+globalConfig.getPackageServiceUp()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }
    /**
     * 构建外表的service
     */
    default void buildForeign(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        RelationTable foreign = relationConfig.getForeign();
        buildJavaMethods.add(foreignSelectPrimaryAndForeign(autoCodeConfig));
        buildJavaMethods.add(foreignSelectPrimaryAndForeignByCondition(autoCodeConfig));
        buildJavaMethods.add(foreignDelete(autoCodeConfig));
        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(foreignImports(autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), foreign.getExistParentPackage(),
                globalConfig.getPackageService())+"/"+foreign.getBeanName()+globalConfig.getPackageServiceUp()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }


    /**
     * 开始构建
     */
    default void build(AutoCodeConfig autoCodeConfig){

        BuildJavaConfig p = new BuildJavaConfig();
        BuildJavaConfig f = new BuildJavaConfig();
        custom(autoCodeConfig,p,f);
        buildPrimary(autoCodeConfig,p);
        buildForeign(autoCodeConfig,f);

    }
}
