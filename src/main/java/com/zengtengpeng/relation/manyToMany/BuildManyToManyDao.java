package com.zengtengpeng.relation.manyToMany;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.build.BuildBaseDao;
import com.zengtengpeng.relation.config.RelationConfig;
import com.zengtengpeng.relation.utils.RelationBuilUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建一对多dao
 */
@FunctionalInterface
public interface BuildManyToManyDao extends BuildBaseDao {

    /**
     * 主表导入
     * @param autoCodeConfig
     * @return
     */
    default List<String> primaryImports(AutoCodeConfig autoCodeConfig){
        List list=new ArrayList();
        list.add("java.util.List");
        return list;
    }

    /**
     * 主表查询
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod primarySelect(AutoCodeConfig autoCodeConfig) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod buildJavaMethod = select(primary, foreign);
        return buildJavaMethod;
    }

    default BuildJavaMethod select(RelationTable primary, RelationTable foreign) {
        BuildJavaMethod buildJavaMethod=new BuildJavaMethod();
        buildJavaMethod.setRemark(String.format("根据%s查询%s",foreign.getRemark(),primary.getRemark()));
        buildJavaMethod.setReturnType(String.format("List<%s>",primary.getBeanName()));
        buildJavaMethod.setMethodName(String.format("select%sBy%s",primary.getBeanName(),foreign.getBeanName()));
        List<String> params=new ArrayList<>();
        params.add(primary.getBeanName()+" "+primary.getBeanNameLower());
        buildJavaMethod.setParams(params);
        return buildJavaMethod;
    }

    /**
     * 主表删除
     * @return
     */
    default BuildJavaMethod primaryDelete(AutoCodeConfig autoCodeConfig) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod buildJavaMethod = RelationBuilUtils.getDaoBaseDelete(primary, foreign);
        return buildJavaMethod;
    }


    /**
     * 外表查询
     * @return
     */
    default BuildJavaMethod foreignSelect(AutoCodeConfig autoCodeConfig) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable  primary= relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod buildJavaMethod = select(foreign, primary);
        return buildJavaMethod;
    }

    /**
     * 外表导入
     * @return
     */
    default List<String> foreignImports(AutoCodeConfig autoCodeConfig){
        List list=new ArrayList();
        list.add("java.util.List");
        return list;
    }

    /**
     * 构建主表的dao
     */
    default void buildPrimary(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();

        List<BuildJavaMethod> buildJavaMethods =buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.add(primarySelect(autoCodeConfig));
        buildJavaMethods.add(primaryDelete(autoCodeConfig));

        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(primaryImports(autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), primary.getExistParentPackage(),
                globalConfig.getPackageDao())+"/"+primary.getBeanName()+globalConfig.getPackageDaoUp()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }
    /**
     * 构建外表的dao
     */
    default void buildForeign(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.add(foreignSelect(autoCodeConfig));
        buildJavaMethods.add(foreignDelete(autoCodeConfig));

        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(foreignImports(autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), foreign.getExistParentPackage(),
                globalConfig.getPackageDao())+"/"+foreign.getBeanName()+globalConfig.getPackageDaoUp()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }

}
