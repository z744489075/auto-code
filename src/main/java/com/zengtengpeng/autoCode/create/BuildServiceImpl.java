package com.zengtengpeng.autoCode.create;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.jdbc.bean.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成servceImpl
 */
@FunctionalInterface
public interface BuildServiceImpl {


    default BuildServiceImpl before(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        StringBuffer content = buildJavaConfig.getContent();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        MyStringUtils.append(content, "package %s.%s.impl;", globalConfig.getParentPack(), globalConfig.getPackageService());
        return this;
    }

    /**
     * 构建导入包
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildServiceImpl buildImports(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        StringBuffer content = buildJavaConfig.getContent();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        Bean bean = autoCodeConfig.getBean();
        List<String> imports = buildJavaConfig.getImports();
        if (imports == null) {
            imports = new ArrayList<>();
        }
        if (buildJavaConfig.getDefaultRealize()) {
            imports.add(globalConfig.getParentPack() + "." + globalConfig.getPackageDao() + "." + bean.getTableName() + MyStringUtils.firstUpperCase(globalConfig.getPackageDao()));
            imports.add(globalConfig.getParentPack() + "." + globalConfig.getPackageService() + "." + bean.getTableName() + MyStringUtils.firstUpperCase(globalConfig.getPackageService()));
            imports.add("org.springframework.stereotype.Service");
            imports.add("org.springframework.transaction.annotation.Transactional");
            imports.add("javax.annotation.Resource");
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
    default BuildServiceImpl buildClass(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
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
                " *" +buildJavaConfig.getRemark()+" serverImpl"+
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
            String serviceName = bean.getTableName() + MyStringUtils.firstUpperCase(autoCodeConfig.getGlobalConfig().getPackageService());
            implement.add(serviceName);

            //注解
            annotations.add("@Service");
            annotations.add("@Transactional");


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
        MyStringUtils.append(content, "public class %s%sImpl  %s %s {\n\n",
                bean.getTableName(), MyStringUtils.firstUpperCase(globalConfig.getPackageService()), s1, s2);
        return this;
    }

    /**
     * 构建字段
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildServiceImpl buildField(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        StringBuffer content = buildJavaConfig.getContent();
        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        if (buildJavaFields == null) {
            buildJavaFields = new ArrayList<>();
        }
        if (buildJavaConfig.getDefaultRealize()) {
            Bean bean = autoCodeConfig.getBean();
            GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();

            BuildJavaField buildJavaField = new BuildJavaField();
            buildJavaField.setReturnType(bean.getTableName() + globalConfig.getPackageDaoUp());
            buildJavaField.setFiledType("private");
            buildJavaField.setFiledName(bean.getTableValue() + globalConfig.getPackageDaoUp());
            List<String> an = new ArrayList<>();
            an.add("@Resource");
            buildJavaField.setAnnotation(an);
            buildJavaField.setRemark("注入dao");
            buildJavaFields.add(buildJavaField);
            buildJavaConfig.setBuildJavaFields(buildJavaFields);
        }
        BuildUtils.buildFields(buildJavaConfig.getBuildJavaFields(), content);
        return this;
    }

    /**
     * 构建方法
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildServiceImpl buildMethods(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        StringBuffer content = buildJavaConfig.getContent();
        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        if (buildJavaMethods == null) {
            buildJavaMethods = new ArrayList<>();
        }

        if (buildJavaConfig.getDefaultRealize()) {
            Bean bean = autoCodeConfig.getBean();
            GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
            BuildJavaMethod buildJavaMethod = new BuildJavaMethod();

            List<String> an = new ArrayList<>();
            an.add("@Override");
            buildJavaMethod.setAnnotation(an);
            buildJavaMethod.setReturnType(bean.getTableName() + globalConfig.getPackageDaoUp());
            buildJavaMethod.setMethodType("public");
            buildJavaMethod.setMethodName("initDao");
            buildJavaMethod.setContent("return " + bean.getTableValue() + globalConfig.getPackageDaoUp() + ";");
            buildJavaMethod.setRemark("初始化");
            buildJavaMethods.add(buildJavaMethod);
            buildJavaConfig.setBuildJavaMethods(buildJavaMethods);
        }
        BuildUtils.buildMethods(buildJavaConfig.getBuildJavaMethods(), content);
        return this;
    }


    /**
     * 结束
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildServiceImpl end(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
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
