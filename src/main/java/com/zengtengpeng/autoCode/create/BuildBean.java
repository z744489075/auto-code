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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成bean
 */
@FunctionalInterface
public interface BuildBean {


    Logger logger = LoggerFactory.getLogger(BuildBean.class);

    default BuildBean before(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        StringBuffer content = buildJavaConfig.getContent();
        MyStringUtils.append(content, "package %s.%s;", globalConfig.getParentPack(), globalConfig.getPackageBean());
        return this;
    }

    /**
     * 构建导入包
     *
     * @param autoCodeConfig
     */
    default BuildBean buildImports(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {

        StringBuffer content = buildJavaConfig.getContent();
        List<String> imports = buildJavaConfig.getImports();
        if (imports == null) {
            imports = new ArrayList<>();
        }
        if (buildJavaConfig.getDefaultRealize()) {
            imports.add("com.fasterxml.jackson.annotation.JsonIgnore");
            imports.add("com.zengtengpeng.common.bean.Page");
            imports.add("com.zengtengpeng.common.utils.DateUtils");
            imports.add("java.util.Date");
            imports.add("java.math.BigDecimal");
            imports.add("com.zengtengpeng.autoCode.utils.MyStringUtils");
            if(autoCodeConfig.getGlobalConfig().getSwagger()){
                imports.add("io.swagger.annotations.ApiModel");
                imports.add("io.swagger.annotations.ApiModelProperty");
            }
        }
        imports.forEach(t -> content.append("import " + t + ";\n"));

        content.append("\n\n");
        return this;
    }

    /**
     * 构建class
     *
     * @param autoCodeConfig
     */
    default BuildBean buildClass(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {

        Bean bean = autoCodeConfig.getBean();
        StringBuffer isb = new StringBuffer();
        List<String> extend = buildJavaConfig.getExtend();
        if (extend == null) {
            extend = new ArrayList<>();
        }
        StringBuffer content = buildJavaConfig.getContent();
        if(MyStringUtils.isEmpty(buildJavaConfig.getRemark())){
            buildJavaConfig.setRemark(bean.getTableRemarks());
        }
        content.append("/**\n" +
                " *" +buildJavaConfig.getRemark()+" bean"+
                "\n */\n");

        List<String> implement = buildJavaConfig.getImplement();

        if (implement == null) {
            implement = new ArrayList<>();
        }

        List<String> annotations = buildJavaConfig.getAnnotations();
        if (annotations == null) {
            annotations = new ArrayList<>();
        }
        if(autoCodeConfig.getGlobalConfig().getSwagger()){
            annotations.add("@ApiModel(description=\""+bean.getTableRemarks()+"\")");
        }
        if (buildJavaConfig.getDefaultRealize()) {
            extend.add("Page");
        }

        implement.forEach(t -> isb.append(t + ","));

        String s1 = "";
        if (extend.size() > 0) {
            s1 = " extends " + extend.get(0);
        }

        String s2 = "";
        if (isb.length() > 0) {
            s2 = "implements " + isb.substring(0, isb.length() - 1);
        }


        annotations.forEach(t -> content.append(t + "\n"));
        MyStringUtils.append(content, "public class %s  %s %s {\n\n",
                bean.getTableName(),  s1, s2);
        return this;
    }

    /**
     * 构建字段
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildBean buildField(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        if (buildJavaFields == null) {
            buildJavaFields = new ArrayList<>();
        }
        if (buildJavaConfig.getDefaultRealize()) {
            Bean bean = autoCodeConfig.getBean();
            List<BuildJavaField> finalBuildJavaFields = buildJavaFields;
            bean.getAllColumns().forEach(t->{
                BuildJavaField buildJavaField = new BuildJavaField();
                List<String> an=new ArrayList<>();
                if(autoCodeConfig.getGlobalConfig().getSwagger()){
                    an.add("@ApiModelProperty(value = \""+t.getRemarks().replace("\"","\\\"")+"\")");
                }
                buildJavaField.setAnnotation(an);
                buildJavaField.setReturnType(t.getBeanType_());
                buildJavaField.setFiledType("private");
                buildJavaField.setFiledName(t.getBeanName());
                buildJavaField.setRemark(t.getRemarks());
                finalBuildJavaFields.add(buildJavaField);
            });

            buildJavaConfig.setBuildJavaFields(finalBuildJavaFields);
        }
        StringBuffer content = buildJavaConfig.getContent();
        BuildUtils.buildFields(buildJavaConfig.getBuildJavaFields(), content);
        return this;
    }

    /**
     * 构建方法
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildBean buildMethods(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {

        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        if (buildJavaMethods == null) {
            buildJavaMethods = new ArrayList<>();
        }

        if (buildJavaConfig.getDefaultRealize()) {
            Bean bean = autoCodeConfig.getBean();
            List<BuildJavaMethod> finalBuildJavaMethods = buildJavaMethods;

            Map cons=new HashMap();
            bean.setCons(cons);
            bean.getAllColumns().forEach(t->{
                //get
                BuildJavaMethod get = new BuildJavaMethod();

                //如果是date则坐下处理
                if("Date".equals(t.getBeanType_())){
                    /*List<String> an=new ArrayList<>();
                    an.add("@JsonIgnore");
                    get.setAnnotation(an);*/

                    BuildJavaMethod get1 = new BuildJavaMethod();
                    get1.setReturnType("String");
                    get1.setMethodType("public");
                    get1.setMethodName("get"+t.getBeanName_()+"_");
                    if("DATE".equals(t.getJdbcType_())){
                        get1.setContent("return DateUtils.formatDate(" +t.getBeanName()+");");
                    }else {
                        get1.setContent("return DateUtils.formatDateTime(" +t.getBeanName()+");");
                    }
                    finalBuildJavaMethods.add(get1);
                }

                //如果注释是json格式的则转成key value
                //转换json
                ObjectMapper objectMapper=new ObjectMapper();
                String remarks = t.getRemarks();

                try {
                    Map<Object,Object> map = objectMapper.readValue(remarks, Map.class);

                    logger.info("字段成功转成json->"+t.getBeanName()+"注释->"+t.getRemarks()+"");
                    t.setJson(true);
                    /*List<String> an=new ArrayList<>();
                    an.add("@JsonIgnore");
                    get.setAnnotation(an);*/

                    BuildJavaMethod get1 = new BuildJavaMethod();
                    get1.setReturnType("String");
                    get1.setMethodType("public");
                    get1.setMethodName("get"+t.getBeanName_()+"_");
                    StringBuffer json=new StringBuffer();
                    json.append("if(MyStringUtils.isEmpty("+t.getBeanName()+")){\n");
                    MyStringUtils.append(json," return \"\";",3);

                    for (Map.Entry me : map.entrySet()) {
                        if("name".equals(me.getKey())){
                            continue;
                        }
                        MyStringUtils.append(json,"}else if(%s.toString().equals(\"%s\")){",2,t.getBeanName(),me.getKey().toString());
                        MyStringUtils.append(json,"return \"%s\";",3,me.getValue().toString());
                    }
                    MyStringUtils.append(json,"}",2);
                    MyStringUtils.append(json,"return \"\";",2);
                    get1.setContent(json.toString());
                    finalBuildJavaMethods.add(get1);

                    cons.put(t.getBeanName(),map);
                } catch (Exception e) {
                }
                get.setReturnType(t.getBeanType_());
                get.setMethodType("public");
                get.setMethodName("get"+t.getBeanName_());
                get.setContent("return " +t.getBeanName()+";");
                finalBuildJavaMethods.add(get);

                //set
                BuildJavaMethod set = new BuildJavaMethod();
                set.setReturnType("void");
                set.setMethodType("public");
                set.setMethodName("set"+t.getBeanName_());
                List<String> params=new ArrayList<>();
                params.add(String.format("%s %s",t.getBeanType_(),t.getBeanName()));
                set.setParams(params);
                set.setContent(String.format("this.%s=%s;",t.getBeanName(),t.getBeanName()));
                finalBuildJavaMethods.add(set);
            });

            buildJavaConfig.setBuildJavaMethods(finalBuildJavaMethods);
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
    default BuildBean end(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
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
        if(buildJavaConfig == null){
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
