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
 * 生成dao
 */
@FunctionalInterface
public interface BuildDao {

    StringBuffer stringBuffer = new StringBuffer();
    default BuildDao before(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        MyStringUtils.append(stringBuffer,"package %s.%s;",globalConfig.getParentPack(),globalConfig.getPackageDao());
        return this;
    }

    /**
     * 构建导入包
     * @param autoCodeConfig
     * @return
     */
    default BuildDao buildImports(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig){
        if(buildJavaConfig==null){
            buildJavaConfig=new BuildJavaConfig();
        }
        Bean bean = autoCodeConfig.getBean();
        List<String> imports = buildJavaConfig.getImports();
        if(imports==null){
            imports=new ArrayList<>();
        }
        if(buildJavaConfig.getDefaultRealize()){
            imports.add("com.zengtengpeng.common.dao.BaseDao");
            imports.add(autoCodeConfig.getGlobalConfig().getParentPack()+".bean."+bean.getTableName());
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
    default BuildDao buildClass(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig) {
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
            extend.add("BaseDao<" + bean.getTableName() + ">");
        }
        extend.forEach(t-> s.append(t+","));
        List<String> annotations = buildJavaConfig.getAnnotations();

        if(annotations!=null){
            annotations.forEach(t->stringBuffer.append(t+"\n"));
        }
        String s1="";
        if(s.length()>0){
            s1="extends "+s.substring(0,s.length()-1);
        }

        MyStringUtils.append(stringBuffer,"public interface %s%s  %s {\n\n",
                bean.getTableName(),MyStringUtils.firstUpperCase(globalConfig.getPackageDao()),s1);
        return this;
    }


    /**
     * 结束
     * @param autoCodeConfig
     * @return
     */
    default BuildDao end(AutoCodeConfig autoCodeConfig,BuildJavaConfig buildJavaConfig){
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
     * 构建dao
     * @param autoCodeConfig
     * @return
     */
    default String buildDao(AutoCodeConfig autoCodeConfig){
        BuildJavaConfig buildJavaConfig = custom(autoCodeConfig);
        BuildDao before = before(autoCodeConfig,buildJavaConfig).buildImports(autoCodeConfig,buildJavaConfig).buildClass(autoCodeConfig,buildJavaConfig);
        BuildUtils.buildCustom(buildJavaConfig,stringBuffer);

        before.end(autoCodeConfig,buildJavaConfig);
        return stringBuffer.toString();
    }


}
