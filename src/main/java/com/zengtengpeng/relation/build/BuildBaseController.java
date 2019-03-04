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
 * 构建一对一Controller
 */

@FunctionalInterface
public interface BuildBaseController {

    Logger logger = LoggerFactory.getLogger(BuildBaseController.class);

    /**
     * 自定义构建
     * @param autoCodeConfig 关系描述配置
     * @param primaryBuildJavaConfig 主表自定义配置
     * @param foreignBuildJavaConfig 外表自定义配置
     */
    void custom(AutoCodeConfig autoCodeConfig, BuildJavaConfig primaryBuildJavaConfig, BuildJavaConfig foreignBuildJavaConfig);


    /**
     * 构建外表 级联查询(带分页) 默认 page=1 pageSize等于10 详见 Page类(所有bean都继承该类)
     * @return
     */
    default BuildJavaMethod foreignSelect(AutoCodeConfig autoCodeConfig){

        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@ResponseBody");
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        an.add(String.format("@RequestMapping(\"%s/select%sAnd%s\")", foreign.getBeanNameLower(), primaryKeyBeanName, foreignBeanName));
        foreignSelect.setAnnotation(an);
        foreignSelect.setReturnType("DataRes");
        foreignSelect.setMethodType("public");
        foreignSelect.setMethodName(String.format("select%sAnd%s", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(foreignBeanName +" "+ foreign.getBeanNameLower());
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        foreignSelect.setParams(params);
        foreignSelect.setContent(String.format("return DataRes.success(%sService.select%sAnd%s(%s));", foreign.getBeanNameLower(), primaryKeyBeanName, foreignBeanName, foreign.getBeanNameLower()));
        foreignSelect.setRemark("级联查询(带分页) "+primary.getRemark()+"--"+foreign.getRemark());
        return foreignSelect;
    }

    /**
     * 外表级联条件查询
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod foreignSelectByCondition(AutoCodeConfig autoCodeConfig){

        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@ResponseBody");
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        an.add(String.format("@RequestMapping(\"%s/select%sAnd%sByCondition\")", foreign.getBeanNameLower(), primaryKeyBeanName, foreignBeanName));
        foreignSelect.setAnnotation(an);
        foreignSelect.setReturnType("DataRes");
        foreignSelect.setMethodType("public");
        foreignSelect.setMethodName(String.format("select%sAnd%sByCondition", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(foreignBeanName +" "+ foreign.getBeanNameLower());
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        foreignSelect.setParams(params);
        foreignSelect.setContent(String.format("return DataRes.success(%sService.select%sAnd%sByCondition(%s));", foreign.getBeanNameLower(), primaryKeyBeanName, foreignBeanName, foreign.getBeanNameLower()));
        foreignSelect.setRemark("级联条件查询 "+primary.getRemark()+"--"+foreign.getRemark());
        return foreignSelect;
    }


    /**
     * 构建主表 级联查询(带分页)  默认 page=1 pageSize等于10 详见 Page类(所有bean都继承该类)
     * @return
     */
    default BuildJavaMethod primarySelect(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@ResponseBody");
        String primaryKeyBeanName_ =primary.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
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
        foreignSelect.setRemark("级联查询(带分页) "+primary.getRemark()+"--"+foreign.getRemark());
        return foreignSelect;
    }
    /**
     * 构建主表 级联条件查询
     * @return
     */
    default BuildJavaMethod primarySelectByCondition(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@ResponseBody");
        String primaryKeyBeanName_ =primary.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        an.add(String.format("@RequestMapping(\"%s/select%sAnd%sByCondition\")", primaryKeyBeanName_, primaryKeyBeanName, foreignBeanName));
        foreignSelect.setAnnotation(an);
        foreignSelect.setReturnType("DataRes");
        foreignSelect.setMethodType("public");
        foreignSelect.setMethodName(String.format("select%sAnd%sByCondition", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(primaryKeyBeanName +" "+ primaryKeyBeanName_);
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        foreignSelect.setParams(params);
        foreignSelect.setContent(String.format("return DataRes.success(%sService.select%sAnd%sByCondition(%s));", primaryKeyBeanName_, primaryKeyBeanName, foreignBeanName, primaryKeyBeanName_));
        foreignSelect.setRemark("级联条件查询 "+primary.getRemark()+"--"+foreign.getRemark());
        return foreignSelect;
    }



    /**
     * 主表级联删除(根据主键删除)
     * @return
     */
    default BuildJavaMethod primaryDelete(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        String primaryKeyBeanName_ = primary.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
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
        List<String> an = new ArrayList<>();
        an.add("@ResponseBody");
        an.add(String.format("@RequestMapping(\"%s/delete%sAnd%s\")", foreign.getBeanNameLower(), primaryKeyBeanName, foreignBeanName));
        deleteTestUserAndTestClass.setAnnotation(an);
        deleteTestUserAndTestClass.setReturnType("DataRes");
        deleteTestUserAndTestClass.setMethodType("public");
        deleteTestUserAndTestClass.setMethodName(String.format("delete%sAnd%s", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(foreignBeanName +" "+ foreign.getBeanNameLower());
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        deleteTestUserAndTestClass.setParams(params);
        deleteTestUserAndTestClass.setContent(String.format("return DataRes.success(%sService.delete%sAnd%s(%s));", foreign.getBeanNameLower(), primaryKeyBeanName, foreignBeanName, foreign.getBeanNameLower()));
        deleteTestUserAndTestClass.setRemark("级联删除(根据主键删除) "+primary.getRemark()+"--"+foreign.getRemark());
        return deleteTestUserAndTestClass;
    }



    /**
     * 构建主表的controller
     */
    default void buildPrimary(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        List<BuildJavaMethod> buildJavaMethods =buildJavaConfig.getBuildJavaMethods();
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        buildJavaMethods.add(primarySelect(autoCodeConfig));
        buildJavaMethods.add(primarySelectByCondition(autoCodeConfig));
        buildJavaMethods.add(primaryDelete(autoCodeConfig));

        List<String> imports = buildJavaConfig.getImports();

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), primary.getExistParentPackage(),
                globalConfig.getPackageController())+"/"+primary.getBeanName()+globalConfig.getPackageControllerUp()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }
    /**
     * 构建外表的controller
     */
    default void buildForeign(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        buildJavaMethods.add(foreignSelect(autoCodeConfig));
        buildJavaMethods.add(foreignSelectByCondition(autoCodeConfig));
        buildJavaMethods.add(foreignDelete(autoCodeConfig));
        List<String> imports = buildJavaConfig.getImports();

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), foreign.getExistParentPackage(),
                globalConfig.getPackageController())+"/"+foreign.getBeanName()+globalConfig.getPackageControllerUp()+".java";
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
