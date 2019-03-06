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
 * 生成dao
 */
@FunctionalInterface
public interface BuildDao {

    default BuildDao before(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        StringBuffer content = buildJavaConfig.getContent();
        MyStringUtils.append(content,"package %s.%s;",globalConfig.getParentPack(),globalConfig.getPackageDao());
        return this;
    }

    /**
     * 构建导入包
     * @param autoCodeConfig
     * @return
     */
    default BuildDao buildImports(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig){

        Bean bean = autoCodeConfig.getBean();
        List<String> imports = buildJavaConfig.getImports();
        if(imports==null){
            imports=new ArrayList<>();
        }
        if(buildJavaConfig.getDefaultRealize()){
            imports.add("com.zengtengpeng.common.dao.BaseDao");
            imports.add("org.apache.ibatis.annotations.Mapper");
            GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
            imports.add(globalConfig.getParentPack()+"."+globalConfig.getPackageBean()+"."+bean.getTableName());
        }
        StringBuffer content = buildJavaConfig.getContent();
        imports.forEach(t->content.append("import "+t+";\n"));

        content.append("\n\n");
        return this;
    }

    /**
     * 构建class
     * @param autoCodeConfig
     * @return
     */
    default BuildDao buildClass(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig) {

        Bean bean = autoCodeConfig.getBean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        StringBuffer s=new StringBuffer();
        List<String> extend = buildJavaConfig.getExtend();
        if(extend==null){
            extend=new ArrayList<>();
        }
        if(buildJavaConfig.getDefaultRealize()) {
            extend.add("BaseDao<" + bean.getTableName() + ">");
        }
        if(MyStringUtils.isEmpty(buildJavaConfig.getRemark())){
            buildJavaConfig.setRemark(bean.getTableRemarks());
        }
        StringBuffer content = buildJavaConfig.getContent();
        content.append("/**\n" +
                " *" +buildJavaConfig.getRemark()+" dao"+
                "\n */\n");
        extend.forEach(t-> s.append(t+","));
        List<String> annotations = buildJavaConfig.getAnnotations();
        if(annotations==null){
            annotations=new ArrayList<>();
        }
        annotations.add("@Mapper");

        annotations.forEach(t->content.append(t+"\n"));
        String s1="";
        if(s.length()>0){
            s1=" extends "+s.substring(0,s.length()-1);
        }

        MyStringUtils.append(content,"public interface %s%s %s{\n\n",
                bean.getTableName(),MyStringUtils.firstUpperCase(globalConfig.getPackageDao()),s1);
        return this;
    }

    /**
     * 构建字段
     * @param autoCodeConfig
     * @return
     */
    default BuildDao buildField(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig) {
        StringBuffer content = buildJavaConfig.getContent();
        BuildUtils.buildFields(buildJavaConfig.getBuildJavaFields(),content);
        return this;
    }
    /**
     * 构建方法
     * @param autoCodeConfig
     * @return
     */
    default BuildDao buildMethods(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig) {
        StringBuffer content = buildJavaConfig.getContent();
        BuildUtils.buildMethods(buildJavaConfig.getBuildJavaMethods(),content);
        return this;
    }
    /**
     * 结束
     * @param autoCodeConfig
     * @return
     */
    default BuildDao end(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig){
        StringBuffer content = buildJavaConfig.getContent();
        content.append("}\n");
        return this;
    }

    /**
     * 自定义方法
     * @param
     * @return
     */
    BuildJavaConfig custom(AutoCodeConfig autoCodeConfig);

    /**
     * 构建dao
     * @param autoCodeConfig
     * @return
     */
    default String build(AutoCodeConfig autoCodeConfig){
        BuildJavaConfig buildJavaConfig = custom(autoCodeConfig);
        if(buildJavaConfig==null){
            buildJavaConfig=new BuildJavaConfig();
        }
         before(autoCodeConfig,buildJavaConfig).buildImports(autoCodeConfig,buildJavaConfig)
                .buildClass(autoCodeConfig,buildJavaConfig).buildField(autoCodeConfig,buildJavaConfig)
                .buildMethods(autoCodeConfig,buildJavaConfig).end(autoCodeConfig,buildJavaConfig);
        StringBuffer content = buildJavaConfig.getContent();
        return content.toString();
    }


}
