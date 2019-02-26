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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 生成controller
 */
@FunctionalInterface
public interface BuildController {

    StringBuffer stringBuffer = new StringBuffer();

    default BuildController before(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        MyStringUtils.append(stringBuffer, "package %s.%s;", globalConfig.getParentPack(), globalConfig.getPackageController());
        return this;
    }

    /**
     * 构建导入包
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildController buildImports(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        if (buildJavaConfig == null) {
            buildJavaConfig = new BuildJavaConfig();
        }
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
            imports.add(bean.getParentPack()+"."+globalConfig.getPackageBean()+"."+bean.getTableName());
            imports.add(bean.getParentPack()+"."+globalConfig.getPackageService()+"."+bean.getTableName()+globalConfig.getPackageService_());
        }
        imports.forEach(t -> stringBuffer.append("import " + t + ";\n"));

        stringBuffer.append("\n\n");
        return this;
    }

    /**
     * 构建class
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildController buildClass(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        if (buildJavaConfig == null) {
            buildJavaConfig = new BuildJavaConfig();
        }
        Bean bean = autoCodeConfig.getBean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        StringBuffer extendsb = new StringBuffer();
        StringBuffer isb = new StringBuffer();
        List<String> extend = buildJavaConfig.getExtend();
        if (extend == null) {
            extend = new ArrayList<>();
        }

        if(MyStringUtils.isEmpty(buildJavaConfig.getRemark())){
            buildJavaConfig.setRemark(bean.getTableRemarks());
        }
        stringBuffer.append("/**\n" +
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

        if (buildJavaConfig.getDefaultRealize()) {
            //注解
            annotations.add("@Controller");

        }
        extend.forEach(t -> extendsb.append(t + ","));

        implement.forEach(t -> isb.append(t + ","));

        String s1 = "";
        if (extendsb.length() > 0) {
            s1 = " extends " + extendsb.substring(0, extendsb.length() - 1);
        }

        String s2 = "";
        if (isb.length() > 0) {
            s2 = " implements " + isb.substring(0, isb.length() - 1);
        }

        annotations.forEach(t -> stringBuffer.append(t + "\n"));
        MyStringUtils.append(stringBuffer, "public class %s%s %s %s{\n\n",
                bean.getTableName(),globalConfig.getPackageController_(), s1, s2);
        return this;
    }

    /**
     * 构建字段
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildController buildField(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        if (buildJavaConfig == null) {
            buildJavaConfig = new BuildJavaConfig();
        }
        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        if (buildJavaFields == null) {
            buildJavaFields = new ArrayList<>();
        }
        if (buildJavaConfig.getDefaultRealize()) {
            Bean bean = autoCodeConfig.getBean();
            GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();

            BuildJavaField buildJavaField = new BuildJavaField();
            buildJavaField.setReturnType(bean.getTableName() + globalConfig.getPackageService_());
            buildJavaField.setFiledType("private");
            buildJavaField.setFiledName(bean.getTableValue() + globalConfig.getPackageService_());
            List<String> an = new ArrayList<>();
            an.add("@Resource");
            buildJavaField.setAnnotation(an);
            buildJavaFields.add(buildJavaField);
            buildJavaConfig.setBuildJavaFields(buildJavaFields);
        }
        BuildUtils.buildField(buildJavaConfig, stringBuffer);
        return this;
    }

    /**
     * 构建方法
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildController buildMethods(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        if (buildJavaConfig == null) {
            buildJavaConfig = new BuildJavaConfig();
        }
        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        if (buildJavaMethods == null) {
            buildJavaMethods = new ArrayList<>();
        }

        Bean bean = autoCodeConfig.getBean();
        if(MyStringUtils.isEmpty(buildJavaConfig.getRemark())){
            buildJavaConfig.setRemark(bean.getTableRemarks());
        }
        if (buildJavaConfig.getDefaultRealize()) {
            GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();

            //删除
            BuildJavaMethod delete = new BuildJavaMethod();
            List<String> an = new ArrayList<>();
            an.add("@ResponseBody");
            an.add(String.format("@RequestMapping(\"%s/deleteByPrimaryKey\")",bean.getTableValue()));
            delete.setAnnotation(an);
            delete.setReturnType("DataRes");
            delete.setMethodType("public");
            delete.setMethodName("deleteByPrimaryKey");
            List<String> params=new ArrayList<>();
            params.add(bean.getTableName()+" "+bean.getTableValue());
            params.add("HttpServletRequest request");
            params.add("HttpServletResponse response");
            delete.setParams(params);
            delete.setContent(String.format("return DataRes.success(%s%s.deleteByPrimaryKey(%s));",bean.getTableValue(),globalConfig.getPackageService_(),bean.getTableValue()));
            delete.setRemark("删除-"+buildJavaConfig.getRemark());
            buildJavaMethods.add(delete);

            //保存
            BuildJavaMethod save = new BuildJavaMethod();
            an = new ArrayList<>();
            an.add("@ResponseBody");
            an.add(String.format("@RequestMapping(\"%s/save\")",bean.getTableValue()));
            save.setAnnotation(an);
            save.setReturnType("DataRes");
            save.setMethodType("public");
            save.setMethodName("save");
            params=new ArrayList<>();
            params.add(bean.getTableName()+" "+bean.getTableValue());
            params.add("HttpServletRequest request");
            params.add("HttpServletResponse response");
            save.setParams(params);
            StringBuffer content=new StringBuffer();
            BeanColumn beanColumn = bean.getPrimaryKey().get(0);
            MyStringUtils.append(content,"if(%s.get%s()==null){",bean.getTableValue(),beanColumn.getBeanName_());
            MyStringUtils.append(content,"return DataRes.success(%s%s.insert(%s));",3,bean.getTableValue(),globalConfig.getPackageService_(),bean.getTableValue());
            MyStringUtils.append(content,"}",2);
            MyStringUtils.append(content,"return DataRes.success(%s%s.update(%s));",2,bean.getTableValue(),globalConfig.getPackageService_(),bean.getTableValue());
            save.setContent(content.toString());
            save.setRemark("保存->"+buildJavaConfig.getRemark());
            buildJavaMethods.add(save);

            //根据主键查询
            BuildJavaMethod selectByPrimaryKey = new BuildJavaMethod();
            an = new ArrayList<>();
            an.add("@ResponseBody");
            an.add(String.format("@RequestMapping(\"%s/selectByPrimaryKey\")",bean.getTableValue()));
            selectByPrimaryKey.setAnnotation(an);
            selectByPrimaryKey.setReturnType("DataRes");
            selectByPrimaryKey.setMethodType("public");
            selectByPrimaryKey.setMethodName("selectByPrimaryKey");
            params=new ArrayList<>();
            params.add(bean.getTableName()+" "+bean.getTableValue());
            params.add("HttpServletRequest request");
            params.add("HttpServletResponse response");
            selectByPrimaryKey.setParams(params);
            selectByPrimaryKey.setRemark("根据主键查询->"+buildJavaConfig.getRemark());
            selectByPrimaryKey.setContent(String.format("return DataRes.success(%s%s.selectByPrimaryKey(%s));",bean.getTableValue(),globalConfig.getPackageService_(),bean.getTableValue()));
            buildJavaMethods.add(selectByPrimaryKey);


            //根据条件查询
            BuildJavaMethod queryByCondition = new BuildJavaMethod();
            an = new ArrayList<>();
            an.add("@ResponseBody");
            an.add(String.format("@RequestMapping(\"%s/queryByCondition\")",bean.getTableValue()));
            queryByCondition.setAnnotation(an);
            queryByCondition.setReturnType("DataRes");
            queryByCondition.setMethodType("public");
            queryByCondition.setMethodName("queryByCondition");
            params=new ArrayList<>();
            params.add(bean.getTableName()+" "+bean.getTableValue());
            params.add("HttpServletRequest request");
            params.add("HttpServletResponse response");
            queryByCondition.setParams(params);
            queryByCondition.setRemark("根据条件查询->"+buildJavaConfig.getRemark());
            queryByCondition.setContent(String.format("return DataRes.success(%s%s.queryByCondition(%s));",bean.getTableValue(),globalConfig.getPackageService_(),bean.getTableValue()));
            buildJavaMethods.add(queryByCondition);

            //分页查询
            BuildJavaMethod selectAllByPaging = new BuildJavaMethod();
            an = new ArrayList<>();
            an.add("@ResponseBody");
            an.add(String.format("@RequestMapping(\"%s/selectAllByPaging\")",bean.getTableValue()));
            selectAllByPaging.setAnnotation(an);
            selectAllByPaging.setReturnType("DataRes");
            selectAllByPaging.setMethodType("public");
            selectAllByPaging.setMethodName("selectAllByPaging");
            params=new ArrayList<>();
            params.add(bean.getTableName()+" "+bean.getTableValue());
            params.add("HttpServletRequest request");
            params.add("HttpServletResponse response");
            selectAllByPaging.setParams(params);
            selectAllByPaging.setRemark("分页查询->"+buildJavaConfig.getRemark());
            selectAllByPaging.setContent(String.format("return DataRes.success(%s%s.selectAllByPaging(%s));",bean.getTableValue(),globalConfig.getPackageService_(),bean.getTableValue()));
            buildJavaMethods.add(selectAllByPaging);



            //导出报表
            BuildJavaMethod export = new BuildJavaMethod();
            an = new ArrayList<>();
            an.add(String.format("@RequestMapping(\"%s/export\")",bean.getTableValue()));
            export.setAnnotation(an);
            export.setReturnType("void");
            export.setMethodType("public");
            export.setMethodName("export");
            params=new ArrayList<>();
            params.add(bean.getTableName()+" "+bean.getTableValue());
            params.add("HttpServletRequest request");
            params.add("HttpServletResponse response");
            export.setParams(params);
            content=new StringBuffer();
            MyStringUtils.append(content,"List<%s> list= %s%s.selectAll(%s);",bean.getTableName(),bean.getTableValue(),globalConfig.getPackageService_(),bean.getTableValue());
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
            buildJavaMethods.add(export);

            buildJavaConfig.setBuildJavaMethods(buildJavaMethods);
        }
        BuildUtils.buildMethods(buildJavaConfig, stringBuffer);
        return this;
    }


    /**
     * 结束
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildController end(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        stringBuffer.append("}\n");
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
        before(autoCodeConfig, buildJavaConfig).
                buildImports(autoCodeConfig, buildJavaConfig)
                .buildClass(autoCodeConfig, buildJavaConfig)
                .buildField(autoCodeConfig, buildJavaConfig)
                .buildMethods(autoCodeConfig, buildJavaConfig)
                .end(autoCodeConfig, buildJavaConfig);

        return stringBuffer.toString();
    }


}
