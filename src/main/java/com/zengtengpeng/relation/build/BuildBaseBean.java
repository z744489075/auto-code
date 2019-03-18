package com.zengtengpeng.relation.build;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.config.RelationConfig;
import com.zengtengpeng.relation.utils.RelationBuilUtils;
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
     * @param autoCodeConfig 关系描述配置
     * @param primaryBuildJavaConfig 主表自定义配置
     * @param foreignBuildJavaConfig 外表自定义配置
     */
    void custom(AutoCodeConfig autoCodeConfig, BuildJavaConfig primaryBuildJavaConfig, BuildJavaConfig foreignBuildJavaConfig);

    /**
     * 主表导入
     * @return
     */
    default List<String> primaryImports(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
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
    default List<BuildJavaField> primaryFields(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        return RelationBuilUtils.getBaseBeanJavaFields(relationConfig.getForeign(),autoCodeConfig.getGlobalConfig());
    }

    /**
     * 主表方法
     * @return
     */
    default List<BuildJavaMethod> primaryMethods(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        return RelationBuilUtils.getBaseBeanJavaMethods(relationConfig.getForeign());
    }


    /**
     * 外表导入
     * @return
     */
    default List<String> foreignImports(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        List<String> im=new ArrayList<>();
        im.add(primary.getExistParentPackage()+"."+autoCodeConfig.getGlobalConfig().getPackageBean()+"."+
                primary.getBeanName());
        im.add("java.util.List");
        return im;
    }
    

    /**
     * 外表字段
     * @return
     */
    default List<BuildJavaField> foreignFields(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        return RelationBuilUtils.getBaseBeanJavaFields(relationConfig.getPrimary(), autoCodeConfig.getGlobalConfig());
    }



    /**
     * 外表方法
     * @return
     */
    default List<BuildJavaMethod> foreignMethods(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        return RelationBuilUtils.getBaseBeanJavaMethods(relationConfig.getPrimary());
    }


    /**
     * 构建主表的bean
     * @param buildJavaConfig
     */
    default void buildPrimary(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        List<BuildJavaMethod> buildJavaMethods =buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.addAll(primaryMethods(autoCodeConfig));

        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(primaryImports(autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        buildJavaFields.addAll(primaryFields(autoCodeConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), primary.getExistParentPackage(),
                globalConfig.getPackageBean())+"/"+primary.getBeanName()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }
    /**
     * 构建外表的bean
     */
    default void buildForeign(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.addAll(foreignMethods(autoCodeConfig));

        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(foreignImports(autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        buildJavaFields.addAll(foreignFields(autoCodeConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), foreign.getExistParentPackage(),
                globalConfig.getPackageBean())+"/"+foreign.getBeanName()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }


    /**
     * 开始构建
     */
    default void build(AutoCodeConfig autoCodeConfig){

        BuildJavaConfig p = new BuildJavaConfig();
        BuildJavaConfig f = new BuildJavaConfig();
        custom(autoCodeConfig,p,f);
        //获取受影响的文件 controller,server,serverImpl dao xml
        buildPrimary(autoCodeConfig,p);
        buildForeign(autoCodeConfig,f);

    }
}
