package com.zengtengpeng.autoCode.create;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.jdbc.bean.Bean;
import com.zengtengpeng.jdbc.bean.BeanColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 生成controller
 */
@FunctionalInterface
public interface BuildController {


    default BuildController before(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        MyStringUtils.append(buildJavaConfig.getContent(), "package %s.%s;", globalConfig.getParentPack(), globalConfig.getPackageController());
        return this;
    }

    /**
     * 构建导入包
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildController buildImports(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {

        StringBuffer content = buildJavaConfig.getContent();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        Bean bean = autoCodeConfig.getBean();
        List<String> imports = buildJavaConfig.getImports();
        if (imports == null) {
            imports = new ArrayList<>();
        }
        if (buildJavaConfig.getDefaultRealize()) {
            imports.add("javax.annotation.Resource");
            imports.add("com.zengtengpeng.common.utils.ExcelUtils");
            imports.add("org.springframework.web.bind.annotation.RequestMapping");
            imports.add("org.springframework.web.bind.annotation.ResponseBody");
            imports.add("javax.servlet.http.HttpServletRequest");
            imports.add("javax.servlet.http.HttpServletResponse");
            imports.add("com.zengtengpeng.common.bean.DataRes");
            imports.add("java.util.List");
            imports.add("java.util.LinkedHashMap");
            imports.add("java.util.Map");
            imports.add("org.springframework.stereotype.Controller");
            if(globalConfig.getSwagger()) {
                imports.add("io.swagger.annotations.Api");
                imports.add("io.swagger.annotations.ApiOperation");
            }
            imports.add(bean.getParentPack()+"."+globalConfig.getPackageBean()+"."+bean.getTableName());
            imports.add(bean.getParentPack()+"."+globalConfig.getPackageService()+"."+bean.getTableName()+globalConfig.getPackageServiceUp());
        }
        imports.forEach(t -> content.append("import " + t + ";\n"));

        content.append("\n\n");
        return this;
    }

    /**
     * 构建class
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildController buildClass(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {

        StringBuffer content = buildJavaConfig.getContent();
        Bean bean = autoCodeConfig.getBean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        StringBuffer isb = new StringBuffer();
        List<String> extend = buildJavaConfig.getExtend();
        if (extend == null) {
            extend = new ArrayList<>();
        }

        if(MyStringUtils.isEmpty(buildJavaConfig.getRemark())){
            buildJavaConfig.setRemark(bean.getTableRemarks());
        }
        content.append("/**\n" +
                " *" +buildJavaConfig.getRemark()+" controller"+
                "\n */\n");

        List<String> implement = buildJavaConfig.getImplement();

        if (implement == null) {
            implement = new ArrayList<>();
        }

        List<String> annotations = buildJavaConfig.getAnnotations();
        if (annotations == null) {
            annotations = new ArrayList<>();
        }
        if(globalConfig.getSwagger()) {
            annotations.add("@Api(description=\""+bean.getTableRemarks()+"\")");
        }

        if (buildJavaConfig.getDefaultRealize()) {
            //注解
            annotations.add("@Controller");

        }
        implement.forEach(t -> isb.append(t + ","));

        String s1 = "";
        if (extend.size() > 0) {
            s1 = " extends " + extend.get(0);
        }

        String s2 = "";
        if (isb.length() > 0) {
            s2 = " implements " + isb.substring(0, isb.length() - 1);
        }

        annotations.forEach(t -> content.append(t + "\n"));
        MyStringUtils.append(content, "public class %s%s %s %s{\n\n",
                bean.getTableName(),globalConfig.getPackageControllerUp(), s1, s2);
        return this;
    }

    /**
     * 构建字段
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildController buildField(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        if (buildJavaFields == null) {
            buildJavaFields = new ArrayList<>();
        }
        if (buildJavaConfig.getDefaultRealize()) {
            Bean bean = autoCodeConfig.getBean();
            GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();

            BuildJavaField buildJavaField = new BuildJavaField();
            buildJavaField.setReturnType(bean.getTableName() + globalConfig.getPackageServiceUp());
            buildJavaField.setFiledType("private");
            buildJavaField.setFiledName(bean.getTableValue() + globalConfig.getPackageServiceUp());
            List<String> an = new ArrayList<>();
            an.add("@Resource");
            buildJavaField.setAnnotation(an);
            buildJavaFields.add(buildJavaField);
            buildJavaConfig.setBuildJavaFields(buildJavaFields);
        }
        StringBuffer content = buildJavaConfig.getContent();
        BuildUtils.buildFields(buildJavaConfig.getBuildJavaFields(), content);
        return this;
    }

    /**
     * 构建根据主键删除方法
     * @param autoCodeConfig
     * @param buildJavaConfig
     * @return
     */
    default BuildJavaMethod buildDelete(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        Bean bean = autoCodeConfig.getBean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();

        BuildJavaMethod delete = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@ResponseBody");
        an.add(String.format("@RequestMapping(\"%s/deleteByPrimaryKey\")",bean.getTableValue()));

        //@ApiOperation(value="删除-测试生成代码", notes="删除-测试生成代码" ,httpMethod="POST")
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"根据主键删除\", notes=\"参数只用到了主键id,其他参数忽略\" ,httpMethod=\"POST\")");
        }
        delete.setAnnotation(an);
        delete.setReturnType("DataRes");
        delete.setMethodType("public");
        delete.setMethodName("deleteByPrimaryKey");
        List<String> params=new ArrayList<>();
        params.add(bean.getTableName()+" "+bean.getTableValue());
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        delete.setParams(params);
        delete.setContent(String.format("return DataRes.success(%s%s.deleteByPrimaryKey(%s));",bean.getTableValue(),globalConfig.getPackageServiceUp(),bean.getTableValue()));
        delete.setRemark("删除-"+buildJavaConfig.getRemark());
        return delete;
    }

    /**
     * 构建保存
     * @param autoCodeConfig
     * @param buildJavaConfig
     * @return
     */
    default BuildJavaMethod buildSave(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        Bean bean = autoCodeConfig.getBean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();

        BuildJavaMethod save = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@ResponseBody");
        an.add(String.format("@RequestMapping(\"%s/save\")",bean.getTableValue()));
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"保存\", notes=\"主键为空则增加否则修改\" ,httpMethod=\"POST\")");
        }
        save.setAnnotation(an);
        save.setReturnType("DataRes");
        save.setMethodType("public");
        save.setMethodName("save");
        List<String> params=new ArrayList<>();
        params.add(bean.getTableName()+" "+bean.getTableValue());
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        save.setParams(params);
        StringBuffer content=new StringBuffer();
        BeanColumn beanColumn = bean.getPrimaryKey().get(0);
        MyStringUtils.append(content,"if(%s.get%s()==null){",bean.getTableValue(),beanColumn.getBeanName_());
        MyStringUtils.append(content,"return DataRes.success(%s%s.insert(%s));",3,bean.getTableValue(),globalConfig.getPackageServiceUp(),bean.getTableValue());
        MyStringUtils.append(content,"}",2);
        MyStringUtils.append(content,"return DataRes.success(%s%s.update(%s));",2,bean.getTableValue(),globalConfig.getPackageServiceUp(),bean.getTableValue());
        save.setContent(content.toString());
        save.setRemark(" 保存 (主键为空则增加否则修改)-> "+buildJavaConfig.getRemark());
        return save;
    }
    /**
     * 构建 根据主键查询
     * @param autoCodeConfig
     * @param buildJavaConfig
     * @return
     */
    default BuildJavaMethod buildSelectByPrimaryKey(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        Bean bean = autoCodeConfig.getBean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();

        BuildJavaMethod selectByPrimaryKey = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@ResponseBody");
        an.add(String.format("@RequestMapping(\"%s/selectByPrimaryKey\")",bean.getTableValue()));
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"根据主键查询\", notes=\"参数只用到了主键.\" ,httpMethod=\"POST\")");
        }
        selectByPrimaryKey.setAnnotation(an);
        selectByPrimaryKey.setReturnType("DataRes");
        selectByPrimaryKey.setMethodType("public");
        selectByPrimaryKey.setMethodName("selectByPrimaryKey");
        List<String> params=new ArrayList<>();
        params.add(bean.getTableName()+" "+bean.getTableValue());
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        selectByPrimaryKey.setParams(params);
        selectByPrimaryKey.setRemark("根据主键查询->"+buildJavaConfig.getRemark());
        selectByPrimaryKey.setContent(String.format("return DataRes.success(%s%s.selectByPrimaryKey(%s));",bean.getTableValue(),globalConfig.getPackageServiceUp(),bean.getTableValue()));
        return selectByPrimaryKey;
    }
    /**
     * 构建 根据条件查询
     * @param autoCodeConfig
     * @param buildJavaConfig
     * @return
     */
    default BuildJavaMethod buildSelectByCondition(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        Bean bean = autoCodeConfig.getBean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();

        BuildJavaMethod selectByCondition = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@ResponseBody");
        an.add(String.format("@RequestMapping(\"%s/selectByCondition\")",bean.getTableValue()));
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"根据条件查询\", notes=\"参数为空则忽略.\" ,httpMethod=\"POST\")");
        }
        selectByCondition.setAnnotation(an);
        selectByCondition.setReturnType("DataRes");
        selectByCondition.setMethodType("public");
        selectByCondition.setMethodName("selectByCondition");
        List<String> params=new ArrayList<>();
        params.add(bean.getTableName()+" "+bean.getTableValue());
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        selectByCondition.setParams(params);
        selectByCondition.setRemark("根据条件查询(所有的实体属性都是条件,如果为空则忽略该字段)->"+buildJavaConfig.getRemark());
        selectByCondition.setContent(String.format("return DataRes.success(%s%s.selectByCondition(%s));",bean.getTableValue(),globalConfig.getPackageServiceUp(),bean.getTableValue()));
        return selectByCondition;
    }
    /**
     * 构建 根据分页查询 默认 page=1 pageSize等于10 详见 Page类(所有bean都继承该类)
     * @param autoCodeConfig
     * @param buildJavaConfig
     * @return
     */
    default BuildJavaMethod buildSelectAllByPaging(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        Bean bean = autoCodeConfig.getBean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();

        BuildJavaMethod selectAllByPaging = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@ResponseBody");
        an.add(String.format("@RequestMapping(\"%s/selectAllByPaging\")",bean.getTableValue()));
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"分页查询\", notes=\"默认page=1pageSize等于10详见Page类(所有bean都继承该类).参数为空则忽略\" ,httpMethod=\"POST\")");
        }
        selectAllByPaging.setAnnotation(an);
        selectAllByPaging.setReturnType("DataRes");
        selectAllByPaging.setMethodType("public");
        selectAllByPaging.setMethodName("selectAllByPaging");
        List<String> params=new ArrayList<>();
        params.add(bean.getTableName()+" "+bean.getTableValue());
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        selectAllByPaging.setParams(params);
        selectAllByPaging.setRemark("分页查询 (所有的实体属性都是条件,如果为空则忽略该字段) (详见Page类.所以的实体都继承该类 默认 page=1 pageSize=10)->"+buildJavaConfig.getRemark());
        selectAllByPaging.setContent(String.format("return DataRes.success(%s%s.selectAllByPaging(%s));",bean.getTableValue(),globalConfig.getPackageServiceUp(),bean.getTableValue()));
        return selectAllByPaging;
    }
    /**
     * 构建 导出excel
     * @param autoCodeConfig
     * @param buildJavaConfig
     * @return
     */
    default BuildJavaMethod buildExport(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        Bean bean = autoCodeConfig.getBean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();

        BuildJavaMethod export = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add(String.format("@RequestMapping(\"%s/export\")",bean.getTableValue()));
        if(globalConfig.getSwagger()){
            an.add("@ApiOperation(value=\"导出excel\", notes=\"导出全部数据.参数为空则忽略.\" ,httpMethod=\"POST\")");
        }
        export.setAnnotation(an);
        export.setReturnType("void");
        export.setMethodType("public");
        export.setMethodName("export");
        List<String> params=new ArrayList<>();
        params.add(bean.getTableName()+" "+bean.getTableValue());
        params.add("HttpServletRequest request");
        params.add("HttpServletResponse response");
        export.setParams(params);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"List<%s> list= %s%s.selectAll(%s);",bean.getTableName(),bean.getTableValue(),globalConfig.getPackageServiceUp(),bean.getTableValue());
        MyStringUtils.append(content,"Map<String, String> header = new LinkedHashMap<>();",2);
        StringBuffer finalContent = content;
        bean.getAllColumns().forEach(t->{
            if("Date".equals(t.getBeanType_())){
                MyStringUtils.append(finalContent,"header.put(\"%s_\", \"%s\");",2,t.getBeanName(),t.getRemarks());
            }else {

                ObjectMapper objectMapper=new ObjectMapper();
                try {
                    Map map = objectMapper.readValue(t.getRemarks(), Map.class);
                    MyStringUtils.append(finalContent,"header.put(\"%s_\", \"%s\");",2,t.getBeanName(),t.getRemarks().replace("\"","\\\""));
                } catch (Exception e) {
                    MyStringUtils.append(finalContent,"header.put(\"%s\", \"%s\");",2,t.getBeanName(),t.getRemarks());
                }
            }

        });
        MyStringUtils.append(finalContent,"ExcelUtils.exportExcel(\"%s\",header,list,response,request);",2,bean.getTableRemarks());
        export.setContent(finalContent.toString());
        export.setRemark("导出报表->"+buildJavaConfig.getRemark());
        return export;
    }
    /**
     * 构建方法
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildController buildMethods(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {

        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        if (buildJavaMethods == null) {
            buildJavaMethods = new ArrayList<>();
        }

        Bean bean = autoCodeConfig.getBean();
        if(MyStringUtils.isEmpty(buildJavaConfig.getRemark())){
            buildJavaConfig.setRemark(bean.getTableRemarks());
        }
        if (buildJavaConfig.getDefaultRealize()) {
            //删除
            buildJavaMethods.add(buildDelete(autoCodeConfig,buildJavaConfig));

            //保存
            buildJavaMethods.add(buildSave(autoCodeConfig,buildJavaConfig));

            //根据主键查询
            buildJavaMethods.add(buildSelectByPrimaryKey(autoCodeConfig,buildJavaConfig));

            //根据条件查询
            buildJavaMethods.add(buildSelectByCondition(autoCodeConfig,buildJavaConfig));

            //分页查询
            buildJavaMethods.add(buildSelectAllByPaging(autoCodeConfig,buildJavaConfig));

            //导出报表
            buildJavaMethods.add(buildExport(autoCodeConfig,buildJavaConfig));

            buildJavaConfig.setBuildJavaMethods(buildJavaMethods);
        }
        StringBuffer content = buildJavaConfig.getContent();
        BuildUtils.buildMethods(buildJavaConfig.getBuildJavaMethods(), content);
        return this;
    }


    /**
     * 结束
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildController end(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        StringBuffer content = buildJavaConfig.getContent();
        content.append("}\n");
        return this;
    }

    /**
     * 自定义方法
     *
     * @param
     * @return
     */
    BuildJavaConfig custom(AutoCodeConfig autoCodeConfig);

    /**
     * 构建serviceImpl
     *
     * @param autoCodeConfig
     * @return
     */
    default String build(AutoCodeConfig autoCodeConfig) {
        BuildJavaConfig buildJavaConfig = custom(autoCodeConfig);
        if(buildJavaConfig==null){
            buildJavaConfig=new BuildJavaConfig();
        }
        before(autoCodeConfig, buildJavaConfig).
                buildImports(autoCodeConfig, buildJavaConfig)
                .buildClass(autoCodeConfig, buildJavaConfig)
                .buildField(autoCodeConfig, buildJavaConfig)
                .buildMethods(autoCodeConfig, buildJavaConfig)
                .end(autoCodeConfig, buildJavaConfig);

        return buildJavaConfig.getContent().toString();
    }


}
