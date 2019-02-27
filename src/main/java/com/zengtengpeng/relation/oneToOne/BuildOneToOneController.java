package com.zengtengpeng.relation.oneToOne;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建一对一Controller
 */

@FunctionalInterface
public interface BuildOneToOneController {

    Logger logger = LoggerFactory.getLogger(BuildOneToOneController.class);

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
    default BuildJavaConfig customPrimary(RelationTable primaryKey,RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return new BuildJavaConfig();
    }
    /**
     * 外表的配置
     * @return
     */
    default BuildJavaConfig customForeign(RelationTable primaryKey,RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return new BuildJavaConfig();
    }



    /**
     * 构建外表 级联查询(带分页)
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod foreignSelect(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return select(foreign, primaryKey);
    }



    /**
     * 构建主表 级联查询(带分页)
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod primarySelect(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return select(primaryKey, foreign);
    }


    default BuildJavaMethod select(RelationTable primaryKey, RelationTable foreign) {
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@ResponseBody");
        String primaryKeyBeanName_ =primaryKey.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primaryKey.getBeanName();
        an.add(String.format("@RequestMapping(\"%s/select%sAnd%s\")", primaryKeyBeanName_, primaryKeyBeanName, foreignBeanName));
        foreignSelect.setAnnotation(an);
        foreignSelect.setReturnType("DataRes");
        foreignSelect.setMethodType("public");
        foreignSelect.setMethodName(String.format("select%sAnd%s", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(primaryKeyBeanName +" "+ primaryKeyBeanName_);
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        foreignSelect.setParams(params);
        foreignSelect.setContent(String.format("return DataRes.success(%sService.select%sAnd%s(%s));", primaryKeyBeanName_, primaryKeyBeanName, foreignBeanName, primaryKeyBeanName_));
        foreignSelect.setRemark("级联查询(带分页) "+primaryKey.getRemark()+"--"+foreign.getRemark());
        return foreignSelect;
    }
    /**
     * 主表级联删除(根据主键删除)
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod primaryDelete(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return delete(primaryKey, foreign);
    }
    /**
     * 外表级联删除(根据主键删除)
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod foreignDelete(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return delete(foreign,primaryKey);
    }

    default BuildJavaMethod delete(RelationTable primaryKey, RelationTable foreign) {
        String primaryKeyBeanName_ = primaryKey.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primaryKey.getBeanName();
        BuildJavaMethod deleteTestUserAndTestClass = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@ResponseBody");
        an.add(String.format("@RequestMapping(\"%s/delete%sAnd%s\")", primaryKeyBeanName_, primaryKeyBeanName, foreignBeanName));
        deleteTestUserAndTestClass.setAnnotation(an);
        deleteTestUserAndTestClass.setReturnType("DataRes");
        deleteTestUserAndTestClass.setMethodType("public");
        deleteTestUserAndTestClass.setMethodName(String.format("delete%sAnd%s", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(primaryKeyBeanName +" "+ primaryKeyBeanName_);
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        deleteTestUserAndTestClass.setParams(params);
        deleteTestUserAndTestClass.setContent(String.format("return DataRes.success(%sService.delete%sAnd%s(%s));", primaryKeyBeanName_, primaryKeyBeanName, foreignBeanName, primaryKeyBeanName_));
        deleteTestUserAndTestClass.setRemark("级联删除(根据主键删除) "+primaryKey.getRemark()+"--"+foreign.getRemark());
        return deleteTestUserAndTestClass;
    }


    /**
     * 构建主表的controller
     * @param primaryKey
     * @param autoCodeConfig
     */
    default void buildPrimary(RelationTable primaryKey,RelationTable foreign, AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        List<BuildJavaMethod> buildJavaMethods =buildJavaConfig.getBuildJavaMethods();

        buildJavaMethods.add(primarySelect(primaryKey,foreign,autoCodeConfig));
        buildJavaMethods.add(primaryDelete(primaryKey,foreign,autoCodeConfig));

        List<String> imports = buildJavaConfig.getImports();

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), primaryKey.getExistParentPackage(),
                globalConfig.getPackageController())+"/"+primaryKey.getBeanName()+globalConfig.getPackageControllerUp()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }
    /**
     * 构建外表的controller
     * @param primaryKey
     * @param autoCodeConfig
     */
    default void buildForeign(RelationTable primaryKey,RelationTable foreign, AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.add(foreignSelect(primaryKey,foreign,autoCodeConfig));
        buildJavaMethods.add(foreignDelete(primaryKey,foreign,autoCodeConfig));
        List<String> imports = buildJavaConfig.getImports();

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), foreign.getExistParentPackage(),
                globalConfig.getPackageController())+"/"+foreign.getBeanName()+globalConfig.getPackageControllerUp()+".java";
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
