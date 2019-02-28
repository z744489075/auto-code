package com.zengtengpeng.relation.oneToOne;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建一对一dao
 */
@FunctionalInterface
public interface BuildOneToOneDao {

    Logger logger = LoggerFactory.getLogger(BuildOneToOneDao.class);

    /**
     * 自定义构建
     * @param primary 主表自定义
     * @param foreign 外表自定义
     */
    void custom(BuildJavaConfig primary,BuildJavaConfig foreign);
    /**
     * 主表的配置
     * @return
     */
    default BuildJavaConfig customPrimary(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return new BuildJavaConfig();
    }
    /**
     * 外表的配置
     * @return
     */
    default BuildJavaConfig customForeign(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return new BuildJavaConfig();
    }


    /**
     * 外表级联删除(根据主键删除)
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod foreignDelete(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        String foreignBeanName = foreign.getBeanName();
        BuildJavaMethod deleteTestUserAndTestClass = new BuildJavaMethod();
        deleteTestUserAndTestClass.setReturnType("Integer");
        deleteTestUserAndTestClass.setMethodName(String.format("deleteBy%s", foreign.getForeignKeyUp(true)));
        List<String> params=new ArrayList<>();
        params.add(foreignBeanName +" "+ foreign.getBeanNameLower());
        deleteTestUserAndTestClass.setParams(params);
        deleteTestUserAndTestClass.setRemark(String.format("根据%s删除%s",foreign.getForeignKeyUp(false),foreign.getRemark()));
        return deleteTestUserAndTestClass;
    }



    /**
     * 构建主表的dao
     * @param primaryKey
     * @param autoCodeConfig
     */
    default void buildPrimary(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        List<BuildJavaMethod> buildJavaMethods =buildJavaConfig.getBuildJavaMethods();

        List<String> imports = buildJavaConfig.getImports();

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), primaryKey.getExistParentPackage(),
                globalConfig.getPackageDao())+"/"+primaryKey.getBeanName()+globalConfig.getPackageDaoUp()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }
    /**
     * 构建外表的dao
     * @param primaryKey
     * @param autoCodeConfig
     */
    default void buildForeign(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.add(foreignDelete(primaryKey,foreign,autoCodeConfig));
        List<String> imports = buildJavaConfig.getImports();

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), foreign.getExistParentPackage(),
                globalConfig.getPackageDao())+"/"+foreign.getBeanName()+globalConfig.getPackageDaoUp()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }


    /**
     * 开始构建
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     */
    default void build(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        BuildJavaConfig p = customPrimary(primaryKey, foreign, autoCodeConfig);

        BuildJavaConfig f = customForeign(primaryKey, foreign, autoCodeConfig);
        custom(p,f);
        buildPrimary(primaryKey,foreign,autoCodeConfig,p);
        buildForeign(primaryKey,foreign,autoCodeConfig,f);

    }
}
