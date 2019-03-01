package com.zengtengpeng.relation.ManyToMany;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.build.BuildBaseDao;
import com.zengtengpeng.relation.config.RelationConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建一对多dao
 */
@FunctionalInterface
public interface BuildManyToManyDao extends BuildBaseDao {

    /**
     * 主表导入
     * @param relationConfig
     * @return
     */
    default List<String> primaryImports(RelationConfig relationConfig){
        List list=new ArrayList();
        list.add("java.util.List");
        return list;
    }

    /**
     * 主表查询
     * @param relationConfig
     * @return
     */
    default BuildJavaMethod primarySelect(RelationConfig relationConfig) {
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
     * @param relationConfig
     * @return
     */
    default BuildJavaMethod primaryDelete(RelationConfig relationConfig) {
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod buildJavaMethod = delete(primary, foreign);
        return buildJavaMethod;
    }

    default BuildJavaMethod delete(RelationTable primary, RelationTable foreign) {
        BuildJavaMethod buildJavaMethod=new BuildJavaMethod();
        buildJavaMethod.setRemark(String.format("根据%s删除%s",foreign.getRemark(),primary.getRemark()));
        buildJavaMethod.setReturnType("Integer");
        buildJavaMethod.setMethodName(String.format("delete%sBy%s",primary.getBeanName(),foreign.getBeanName()));
        List<String> params=new ArrayList<>();
        params.add(primary.getBeanName()+" "+primary.getBeanNameLower());
        buildJavaMethod.setParams(params);
        return buildJavaMethod;
    }

    /**
     * 外表删除
     * @param relationConfig
     * @return
     */
    @Override
    default BuildJavaMethod foreignDelete(RelationConfig relationConfig) {
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod buildJavaMethod = delete(foreign, primary);
        return buildJavaMethod;
    }

    /**
     * 外表查询
     * @param relationConfig
     * @return
     */
    default BuildJavaMethod foreignSelect(RelationConfig relationConfig) {
        RelationTable  primary= relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod buildJavaMethod = select(foreign, primary);
        return buildJavaMethod;
    }

    /**
     * 外表导入
     * @param relationConfig
     * @return
     */
    default List<String> foreignImports(RelationConfig relationConfig){
        List list=new ArrayList();
        list.add("java.util.List");
        return list;
    }

    /**
     * 构建主表的dao
     */
    default void buildPrimary(RelationConfig relationConfig, BuildJavaConfig buildJavaConfig){
        RelationTable primary = relationConfig.getPrimary();
        AutoCodeConfig autoCodeConfig = relationConfig.getAutoCodeConfig();

        List<BuildJavaMethod> buildJavaMethods =buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.add(primarySelect(relationConfig));
        buildJavaMethods.add(primaryDelete(relationConfig));

        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(primaryImports(relationConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), primary.getExistParentPackage(),
                globalConfig.getPackageDao())+"/"+primary.getBeanName()+globalConfig.getPackageDaoUp()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }
    /**
     * 构建外表的dao
     */
    default void buildForeign(RelationConfig relationConfig, BuildJavaConfig buildJavaConfig){
        RelationTable foreign = relationConfig.getForeign();
        AutoCodeConfig autoCodeConfig = relationConfig.getAutoCodeConfig();
        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.add(foreignSelect(relationConfig));
        buildJavaMethods.add(foreignDelete(relationConfig));

        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(foreignImports(relationConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), foreign.getExistParentPackage(),
                globalConfig.getPackageDao())+"/"+foreign.getBeanName()+globalConfig.getPackageDaoUp()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }

}
