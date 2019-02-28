package com.zengtengpeng.autoCode.create;

import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.jdbc.bean.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成servce
 */
@FunctionalInterface
public interface BuildService {


    default BuildService before(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {
        StringBuffer content = buildJavaConfig.getContent();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        MyStringUtils.append(content, "package %s.%s;", globalConfig.getParentPack(), globalConfig.getPackageService());
        return this;
    }

    /**
     * 构建导入包
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildService buildImports(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {

        StringBuffer content = buildJavaConfig.getContent();
        Bean bean = autoCodeConfig.getBean();
        List<String> imports = buildJavaConfig.getImports();
        if (imports == null) {
            imports = new ArrayList<>();
        }
        if (buildJavaConfig.getDefaultRealize()) {
            imports.add("com.zengtengpeng.common.service.BaseService");
            GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
            String parentPack = globalConfig.getParentPack();
            imports.add(parentPack + "." + globalConfig.getPackageBean() + "." + bean.getTableName());
            String daoName = bean.getTableName() + globalConfig.getPackageDaoUp();
            imports.add(parentPack + "." + globalConfig.getPackageDao() + "." + daoName);
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
    default BuildService buildClass(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {

        StringBuffer content = buildJavaConfig.getContent();
        Bean bean = autoCodeConfig.getBean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        StringBuffer s = new StringBuffer();
        List<String> extend = buildJavaConfig.getExtend();
        if (extend == null) {
            extend = new ArrayList<>();
        }
        if(MyStringUtils.isEmpty(buildJavaConfig.getRemark())){
            buildJavaConfig.setRemark(bean.getTableRemarks());
        }
        content.append("/**\n" +
                " *" +buildJavaConfig.getRemark()+" service"+
                "\n */\n");
        if (buildJavaConfig.getDefaultRealize()) {
            String daoName = bean.getTableName() + MyStringUtils.firstUpperCase(autoCodeConfig.getGlobalConfig().getPackageDao());
            extend.add("BaseService<" + bean.getTableName() + "," + daoName + ">");
        }
        extend.forEach(t -> s.append(t + ","));
        List<String> annotations = buildJavaConfig.getAnnotations();

        String s1 = "";
        if (s.length() > 0) {
            s1 = " extends " + s.substring(0, s.length() - 1);
        }
        if (annotations != null) {
            annotations.forEach(t -> content.append(t + "\n"));
        }
        MyStringUtils.append(content, "public interface %s%s%s{\n\n",
                bean.getTableName(), MyStringUtils.firstUpperCase(globalConfig.getPackageService()), s1);
        return this;
    }


    /**
     * 结束
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildService end(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {

        StringBuffer content = buildJavaConfig.getContent();
        content.append("}\n");
        return this;
    }

    /**
     * 构建字段
     *
     * @param autoCodeConfig
     * @return
     */
    default BuildService buildField(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {

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
    default BuildService buildMethods(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig) {

        StringBuffer content = buildJavaConfig.getContent();
        BuildUtils.buildMethods(buildJavaConfig.getBuildJavaMethods(), content);
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
     * 构建service
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
                buildImports(autoCodeConfig, buildJavaConfig).
                buildClass(autoCodeConfig, buildJavaConfig)
                .buildField(autoCodeConfig, buildJavaConfig)
                .buildMethods(autoCodeConfig, buildJavaConfig)
                .end(autoCodeConfig, buildJavaConfig);
        return  buildJavaConfig.getContent().toString();
    }


}
