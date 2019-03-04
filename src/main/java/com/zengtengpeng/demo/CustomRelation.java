package com.zengtengpeng.demo;


import com.zengtengpeng.autoCode.StartCode;
import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.bean.BuildXmlBean;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.enums.XmlElementType;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.oneToOne.*;
import com.zengtengpeng.relation.utils.RelationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多表自定义
 */
public class CustomRelation {
    public static void main(String[] args) {
        //如果单表想要自定义请参见 CustomSimple 类. 里面是如果定义单表的
        StartCode startCode = t -> { };

        //多表自定义
        BuildOneToOne buildOneToOne = new BuildOneToOne() {
            @Override
            public void custom(AutoCodeConfig autoCodeConfig) {

            }

            @Override
            public BuildOneToOneController buildOneToOneController() {
                //Controller autoCodeConfig 全局配置 primaryBuildJavaConfig主表的自定义配置  foreignBuildJavaConfig 外表的自定义配置
                return (autoCodeConfig, primaryBuildJavaConfig, foreignBuildJavaConfig) -> {
                    List<String> imports=new ArrayList<>();
                    imports.add("java.util.HashMap");
                    imports.add("java.util.Hashtable");
                    imports.add("java.util.Collections");
                    //自定义需要导入的类
                    primaryBuildJavaConfig.setImports(imports);

                    List<BuildJavaMethod> methods=new ArrayList<>();
                    BuildJavaMethod method=new BuildJavaMethod();
                    method.setContent("\nSystem.out.println(\"生成完毕\");");
                    method.setMethodName("test");
                    method.setMethodType("public");
                    method.setReturnType("void");
                    List<String> params=new ArrayList<>();
                    params.add("String test");
                    method.setParams(params);
                    method.setRemark("测试生成方法");
                    List<String> ann=new ArrayList<>();
                    ann.add("@SuppressWarnings(\"\")");
                    method.setAnnotation(ann);
                    methods.add(method);
                    //自定义方法 将在类生成如下方法
                    //@SuppressWarnings("")
                    //	public void test(String test){
                    //
                    //System.out.println("生成完毕");
                    //	}
                    primaryBuildJavaConfig.setBuildJavaMethods(methods);


                    List<BuildJavaField> fileds=new ArrayList<>();
                    BuildJavaField jf=new BuildJavaField();
                    jf.setFiledType("private");
                    jf.setReturnType("String");
                    jf.setFiledName("test");
                    jf.setRemark("测试生成字段");
                    jf.setInit("\"初始化字段\"");
                    ann=new ArrayList<>();
                    ann.add("@SuppressWarnings(\"\")");
                    jf.setAnnotation(ann);
                    fileds.add(jf);
                    //自定义字段 将在类生成如下字段
                    //@SuppressWarnings("")
                    //	private String test ="初始化字段";
                    primaryBuildJavaConfig.setBuildJavaFields(fileds);

                    //自定义继承 类单继承 接口多继承
                    List<String> ex=new ArrayList<>();
                    ex.add("Object");
                    primaryBuildJavaConfig.setExtend(ex);
                };
            }

            @Override
            public BuildOneToOneBean buildOneToOneBean() {
                //Bean autoCodeConfig 全局配置 primaryBuildJavaConfig主表的自定义配置  foreignBuildJavaConfig 外表的自定义配置
                return (autoCodeConfig, primaryBuildJavaConfig, foreignBuildJavaConfig) -> {};
            }

            @Override
            public BuildOneToOneDao buildOneToOneDao() {
                //Dao autoCodeConfig 全局配置 primaryBuildJavaConfig主表的自定义配置  foreignBuildJavaConfig 外表的自定义配置
                return (autoCodeConfig, primaryBuildJavaConfig, foreignBuildJavaConfig) -> {};
            }

            @Override
            public BuildOneToOneService buildOneToOneService() {
                //service autoCodeConfig 全局配置 primaryBuildJavaConfig主表的自定义配置  foreignBuildJavaConfig 外表的自定义配置
                return (autoCodeConfig, primaryBuildJavaConfig, foreignBuildJavaConfig) -> {};
            }

            @Override
            public BuildOneToOneServiceImpl buildOneToOneServiceImpl() {
                //serviceImpl autoCodeConfig 全局配置 primaryBuildJavaConfig主表的自定义配置  foreignBuildJavaConfig 外表的自定义配置
                return (autoCodeConfig, primaryBuildJavaConfig, foreignBuildJavaConfig) -> {};
            }

            @Override
            public BuildOneToOneXml BuildOneToOneXml() {
                //xml autoCodeConfig 全局配置  primaryBuildXmlBean 主表需要自定义的方法 foreignBuildXmlBean外表需要自定义的方法
                return (autoCodeConfig, primaryBuildXmlBean, foreignBuildXmlBean) -> {
                    RelationTable primary = autoCodeConfig.getGlobalConfig().getRelationConfig().getPrimary();
                    BuildXmlBean buildXmlBean=new BuildXmlBean();
                    buildXmlBean.setSql("\t\tselect * from "+primary.getDataName());
                    Map<String, String> attrs=new HashMap<>();
                    attrs.put("id",primary.getBeanName()+"test");
                    attrs.put("resultMap","BaseResultMap");
                    buildXmlBean.setAttributes(attrs);
                    buildXmlBean.setXmlElementType(XmlElementType.select);
                    primaryBuildXmlBean.add(buildXmlBean);
                };
            }
        };
        RelationUtils.oneToOne(StartCode.saxYaml("auto-code_one-to-one.yaml"), startCode, buildOneToOne);
    }
}
