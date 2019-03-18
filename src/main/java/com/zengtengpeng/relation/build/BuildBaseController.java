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
    default BuildJavaMethod foreignSelectForeignAndPrimary(AutoCodeConfig autoCodeConfig){

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        an.add(String.format("@RequestMapping(\"%s/select%sAnd%s\")", foreign.getBeanNameLower(), primaryKeyBeanName, foreignBeanName));
        an.add("@ResponseBody");
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"外表级联查询(带分页)\", notes=\"构建外表 级联查询(带分页) 默认 page=1 pageSize等于10 详见 Page类(所有bean都继承该类)\" ,httpMethod=\"POST\")");
        }
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
    default BuildJavaMethod foreignSelectForeignAndPrimaryByCondition(AutoCodeConfig autoCodeConfig){

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        an.add(String.format("@RequestMapping(\"%s/select%sAnd%sByCondition\")", foreign.getBeanNameLower(), primaryKeyBeanName, foreignBeanName));
        an.add("@ResponseBody");
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"外表级联条件查询\", notes=\"外表级联条件查询\" ,httpMethod=\"POST\")");
        }
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
     * 构建 主表级联查询(带分页)  默认 page=1 pageSize等于10 详见 Page类(所有bean都继承该类)
     * @return
     */
    default BuildJavaMethod primarySelectPrimaryAndForeign(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        String primaryKeyBeanName_ =primary.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        an.add(String.format("@RequestMapping(\"%s/select%sAnd%s\")", primaryKeyBeanName_, primaryKeyBeanName, foreignBeanName));
        an.add("@ResponseBody");
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"主表级联查询(带分页)\", notes=\"主表级联查询(带分页)  默认 page=1 pageSize等于10 详见 Page类(所有bean都继承该类)\" ,httpMethod=\"POST\")");
        }
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
     * 构建 主表级联条件查询
     * @return
     */
    default BuildJavaMethod primarySelectPrimaryAndForeignByCondition(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        String primaryKeyBeanName_ =primary.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        an.add(String.format("@RequestMapping(\"%s/select%sAnd%sByCondition\")", primaryKeyBeanName_, primaryKeyBeanName, foreignBeanName));
        an.add("@ResponseBody");
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"主表级联条件查询\", notes=\"主表级联条件查询\" ,httpMethod=\"POST\")");
        }
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
    default BuildJavaMethod primaryDeletePrimaryAndForeign(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        String primaryKeyBeanName_ = primary.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        BuildJavaMethod deleteTestUserAndTestClass = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add(String.format("@RequestMapping(\"%s/delete%sAnd%s\")", primaryKeyBeanName_, primaryKeyBeanName, foreignBeanName));
        an.add("@ResponseBody");
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"主表级联删除(根据主键删除)\", notes=\"主表级联删除(根据主键删除)\" ,httpMethod=\"POST\")");
        }
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
    default BuildJavaMethod foreignDeleteForeignAndPrimary(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        BuildJavaMethod deleteTestUserAndTestClass = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add(String.format("@RequestMapping(\"%s/delete%sAnd%s\")", foreign.getBeanNameLower(), primaryKeyBeanName, foreignBeanName));
        an.add("@ResponseBody");
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"外表级联删除(根据主键删除)\", notes=\"外表级联删除(根据主键删除)\" ,httpMethod=\"POST\")");
        }
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
     * 外表的引入
     * @param autoCodeConfig
     * @return
     */
    default List<String> foreignImports(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        List<String> imports=new ArrayList<>();
        imports.add(primary.getExistParentPackage()+"."+globalConfig.getPackageService()+"."+primary.getBeanName()+globalConfig.getPackageServiceUp());
        imports.add(primary.getExistParentPackage()+"."+globalConfig.getPackageBean()+"."+primary.getBeanName());
        imports.add("java.util.ArrayList");
        return imports;
    }

    /**
     * 外表的字段
     * @param autoCodeConfig
     * @return
     */
    default List<BuildJavaField> foreignFields(AutoCodeConfig autoCodeConfig) {
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        List<BuildJavaField> list = new ArrayList<>();
        BuildJavaField buildJavaField = new BuildJavaField();
        List<String> an = new ArrayList<>();
        an.add("@Resource");
        buildJavaField.setAnnotation(an);
        buildJavaField.setRemark(primary.getRemark());
        buildJavaField.setFiledName(primary.getBeanNameLower() + globalConfig.getPackageServiceUp());
        buildJavaField.setReturnType(primary.getBeanName() + globalConfig.getPackageServiceUp());
        buildJavaField.setFiledType("private");
        list.add(buildJavaField);
        return list;
    }
    /**
     * 主表的引入
     * @param autoCodeConfig
     * @return
     */
    default List<BuildJavaField> primaryFields(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        List<BuildJavaField> list = new ArrayList<>();
        BuildJavaField buildJavaField = new BuildJavaField();
        List<String> an = new ArrayList<>();
        an.add("@Resource");
        buildJavaField.setAnnotation(an);
        buildJavaField.setRemark(foreign.getRemark());
        buildJavaField.setFiledName(foreign.getBeanNameLower() + globalConfig.getPackageServiceUp());
        buildJavaField.setReturnType(foreign.getBeanName() + globalConfig.getPackageServiceUp());
        buildJavaField.setFiledType("private");
        list.add(buildJavaField);
        return list;
    }

    /**
     * 主表的引入
     * @param autoCodeConfig
     * @return
     */
    default List<String> primaryImports(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        List<String> imports=new ArrayList<>();
        imports.add(foreign.getExistParentPackage()+"."+globalConfig.getPackageService()+"."+foreign.getBeanName()+globalConfig.getPackageServiceUp());
        imports.add(foreign.getExistParentPackage()+"."+globalConfig.getPackageBean()+"."+foreign.getBeanName());
        imports.add("java.util.ArrayList");
        return imports;
    }

    /**
     * 构建主表的controller
     */
    default void buildPrimary(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        List<BuildJavaMethod> buildJavaMethods =buildJavaConfig.getBuildJavaMethods();
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        buildJavaMethods.add(primarySelectPrimaryAndForeign(autoCodeConfig));
        buildJavaMethods.add(primarySelectPrimaryAndForeignByCondition(autoCodeConfig));
        buildJavaMethods.add(primaryDeletePrimaryAndForeign(autoCodeConfig));

        List<String> imports = buildJavaConfig.getImports();
        if(imports==null){
            imports=new ArrayList<>();
        }
        imports.addAll(primaryImports(autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        if(buildJavaFields==null){
            buildJavaFields=new ArrayList<>();
        }
        buildJavaFields.addAll(primaryFields(autoCodeConfig));

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
        buildJavaMethods.add(foreignSelectForeignAndPrimary(autoCodeConfig));
        buildJavaMethods.add(foreignSelectForeignAndPrimaryByCondition(autoCodeConfig));
        buildJavaMethods.add(foreignDeleteForeignAndPrimary(autoCodeConfig));
        List<String> imports = buildJavaConfig.getImports();
        if(imports==null){
            imports=new ArrayList<>();
        }
        imports.addAll(foreignImports(autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        if(buildJavaFields==null){
            buildJavaFields=new ArrayList<>();
        }
        buildJavaFields.addAll(foreignFields(autoCodeConfig));
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
