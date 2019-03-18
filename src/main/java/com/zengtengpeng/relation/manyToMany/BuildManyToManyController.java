package com.zengtengpeng.relation.manyToMany;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.build.BuildBaseController;
import com.zengtengpeng.relation.config.RelationConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建一对多Controller
 */

@FunctionalInterface
public interface BuildManyToManyController extends BuildBaseController {

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
        buildJavaMethod.setReturnType("DataRes");
        buildJavaMethod.setMethodType("public");
        buildJavaMethod.setMethodName(String.format("select%sBy%s",foreign.getBeanName(),primary.getBeanName()));
        List<String> params=new ArrayList<>();
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        params.add(foreign.getBeanName()+" "+foreign.getBeanNameLower());
        buildJavaMethod.setParams(params);
        List<String> an=new ArrayList<>();
        an.add(String.format("@RequestMapping(\"%s/select%sBy%s\")",foreign.getBeanNameLower(),foreign.getBeanName(),primary.getBeanName()));
        an.add("@ResponseBody");
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"根据主表id查询外表所有数据(带分页)\", notes=\"根据主表id查询外表所有数据(带分页)\" ,httpMethod=\"POST\")");
        }
        buildJavaMethod.setAnnotation(an);
        buildJavaMethod.setContent(String.format("return DataRes.success(%s%s.select%sBy%s(%s));",
                foreign.getBeanNameLower(),globalConfig.getPackageServiceUp(),foreign.getBeanName(),primary.getBeanName(),foreign.getBeanNameLower()));
        return buildJavaMethod;
    }

    /**
     *  根据外表id查询主表表所有数据(带分页)
     * @return
     */
    default BuildJavaMethod primarySelectByPrimaryForeign(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable primary = relationConfig.getPrimary();
        BuildJavaMethod buildJavaMethod=new BuildJavaMethod();
        buildJavaMethod.setRemark("根据外表id查询主表表所有数据(带分页)");
        buildJavaMethod.setReturnType("DataRes");
        buildJavaMethod.setMethodType("public");
        buildJavaMethod.setMethodName(String.format("select%sBy%s",primary.getBeanName(),foreign.getBeanName()));
        List<String> params=new ArrayList<>();
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        params.add(primary.getBeanName()+" "+primary.getBeanNameLower());
        buildJavaMethod.setParams(params);
        List<String> an=new ArrayList<>();
        an.add(String.format("@RequestMapping(\"%s/select%sBy%s\")",primary.getBeanNameLower(),primary.getBeanName(),foreign.getBeanName()));
        an.add("@ResponseBody");
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"根据外表id查询主表表所有数据(带分页)\", notes=\"根据外表id查询主表表所有数据(带分页)\" ,httpMethod=\"POST\")");
        }
        buildJavaMethod.setAnnotation(an);
        buildJavaMethod.setContent(String.format("return DataRes.success(%s%s.select%sBy%s(%s));",
                primary.getBeanNameLower(),globalConfig.getPackageServiceUp(),primary.getBeanName(),foreign.getBeanName(),primary.getBeanNameLower()));
        return buildJavaMethod;
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
        buildJavaMethods.add(primarySelectByPrimaryForeign(autoCodeConfig));
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
        buildJavaMethods.add(foreignSelectForeignByPrimary(autoCodeConfig));
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

}
