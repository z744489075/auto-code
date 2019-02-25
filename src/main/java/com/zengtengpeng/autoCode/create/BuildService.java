package com.zengtengpeng.autoCode.create;

import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.generator.utils.MyStringUtils;
import com.zengtengpeng.jdbc.bean.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成servce
 */
@FunctionalInterface
public interface BuildService {

    StringBuffer stringBuffer = new StringBuffer();

    default BuildService before(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        MyStringUtils.append(stringBuffer,"package %s.%s;",globalConfig.getParentPack(),globalConfig.getPackageService());
        return this;
    }

    /**
     * 构建导入包
     * @param autoCodeConfig
     * @return
     */
    default BuildService buildImports(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig){
        if(buildJavaConfig==null){
            buildJavaConfig=new BuildJavaConfig();
        }
        Bean bean = autoCodeConfig.getBean();
        List<String> imports = buildJavaConfig.getImports();
        if(imports==null){
            imports=new ArrayList<>();
        }
        if(buildJavaConfig.getDefaultRealize()) {
            imports.add("com.zengtengpeng.common.dao.BaseService");
            String parentPack = autoCodeConfig.getGlobalConfig().getParentPack();
            imports.add(parentPack + ".bean." + bean.getTableName());
            String daoName = bean.getTableName() + MyStringUtils.firstUpperCase(autoCodeConfig.getGlobalConfig().getPackageDao());
            imports.add(parentPack + ".dao." + daoName);
        }
        imports.forEach(t->stringBuffer.append("import "+t+";\n"));

        stringBuffer.append("\n\n");
        return this;
    }

    /**
     * 构建class
     * @param autoCodeConfig
     * @return
     */
    default BuildService buildClass(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig) {
        if(buildJavaConfig==null){
            buildJavaConfig=new BuildJavaConfig();
        }
        Bean bean = autoCodeConfig.getBean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        StringBuffer s=new StringBuffer();
        List<String> extend = buildJavaConfig.getExtend();
        if(extend==null){
            extend=new ArrayList<>();
        }
        if(buildJavaConfig.getDefaultRealize()) {
            String daoName = bean.getTableName() + MyStringUtils.firstUpperCase(autoCodeConfig.getGlobalConfig().getPackageDao());
            extend.add("BaseService<" + bean.getTableName() + "," + daoName + ">");
        }
        extend.forEach(t-> s.append(t+","));
        List<String> annotations = buildJavaConfig.getAnnotations();

        String s1="";
        if(s.length()>0){
            s1="extends "+s.substring(0,s.length()-1);
        }
        if(annotations!=null){
            annotations.forEach(t->stringBuffer.append(t+"\n"));
        }
        MyStringUtils.append(stringBuffer,"public interface %s%s  %s {\n\n",
                bean.getTableName(),MyStringUtils.firstUpperCase(globalConfig.getPackageService()),s1);
        return this;
    }


    /**
     * 结束
     * @param autoCodeConfig
     * @return
     */
    default BuildService end(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig){
        stringBuffer.append("}\n");
        return this;
    }

    /**
     * 自定义sql
     * @param
     * @return
     */
    BuildJavaConfig custom(AutoCodeConfig autoCodeConfig);

    /**
     * 构建service
     * @param autoCodeConfig
     * @return
     */
    default String buildService(AutoCodeConfig autoCodeConfig){
        BuildJavaConfig buildJavaConfig = custom(autoCodeConfig);
        BuildService before = before(autoCodeConfig,buildJavaConfig).buildImports(autoCodeConfig,buildJavaConfig).buildClass(autoCodeConfig,buildJavaConfig);
        BuildUtils.buildCustom(buildJavaConfig,stringBuffer);

        before.end(autoCodeConfig,buildJavaConfig);
        return stringBuffer.toString();
    }


}
