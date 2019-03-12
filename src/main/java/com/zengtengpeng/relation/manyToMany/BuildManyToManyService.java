package com.zengtengpeng.relation.manyToMany;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.build.BuildBaseService;
import com.zengtengpeng.relation.config.RelationConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建多对多service
 */
@FunctionalInterface
public interface BuildManyToManyService extends BuildBaseService {
    /**
     *  根据主表id查询外表所有数据(带分页)
     * @return
     */
    default BuildJavaMethod foreignSelectForeignByPrimary(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod buildJavaMethod=new BuildJavaMethod();
        buildJavaMethod.setRemark("根据主表id查询外表所有数据(带分页)");
        buildJavaMethod.setReturnType(foreign.getBeanName());
        buildJavaMethod.setMethodType("public");
        buildJavaMethod.setMethodName(String.format("select%sBy%s",foreign.getBeanName(),primary.getBeanName()));
        List<String> params=new ArrayList<>();
        params.add(foreign.getBeanName()+" "+foreign.getBeanNameLower());
        buildJavaMethod.setParams(params);
        return buildJavaMethod;
    }

    /**
     *  根据外表id查询主表表所有数据(带分页)
     * @return
     */
    default BuildJavaMethod primarySelectPrimaryByForeign(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable primary = relationConfig.getPrimary();
        BuildJavaMethod buildJavaMethod=new BuildJavaMethod();
        buildJavaMethod.setRemark("根据外表id查询主表表所有数据(带分页)");
        buildJavaMethod.setReturnType(primary.getBeanName());
        buildJavaMethod.setMethodType("public");
        buildJavaMethod.setMethodName(String.format("select%sBy%s",primary.getBeanName(),foreign.getBeanName()));
        List<String> params=new ArrayList<>();
        params.add(primary.getBeanName()+" "+primary.getBeanNameLower());
        buildJavaMethod.setParams(params);
        return buildJavaMethod;
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
        buildJavaMethods.add(primarySelectPrimaryByForeign(autoCodeConfig));

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
        buildJavaMethods.add(foreignSelectForeignByPrimary(autoCodeConfig));
        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(foreignImports(autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), foreign.getExistParentPackage(),
                globalConfig.getPackageService())+"/"+foreign.getBeanName()+globalConfig.getPackageServiceUp()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }
}
