package com.zengtengpeng.demo;

import com.zengtengpeng.autoCode.StartCode;
import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.bean.BuildXmlBean;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.create.*;
import com.zengtengpeng.autoCode.enums.XmlElementType;
import com.zengtengpeng.jdbc.bean.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义单表方法
 */
public class CustomSimple {

    public static void main(String[] args) {
        StartCode startCode=new StartCode() {
            @Override
            public void custom(AutoCodeConfig autoCodeConfig) {
            }

            /**
             * 自定义xml方法
             * @return
             */
            @Override
            public BuildXml BuildXml() {
                //传统写法 二选一
                /*return new BuildXml() {
                    @Override
                    public List<BuildXmlBean> custom(AutoCodeConfig autoCodeConfig) {
                        Bean bean = autoCodeConfig.getBean();
                        List<BuildXmlBean> list=new ArrayList<>();
                        BuildXmlBean buildXmlBean=new BuildXmlBean();
                        buildXmlBean.setSql("\t\tselect * from "+bean.getDataName());
                        Map<String, String> attrs=new HashMap<>();
                        attrs.put("id",bean.getTableValue()+"test");
                        attrs.put("resultMap","BaseResultMap");
                        buildXmlBean.setAttributes(attrs);
                        buildXmlBean.setXmlElementType(XmlElementType.select);
                        list.add(buildXmlBean);
                        return list;
                    }
                };*/
                //lambda表达式写法 二选一
                return t->{
                    //在这里新增自定义xml元素 t是全局配置.参数都在里面
                    //将在类生成如下元素
                    //<select resultMap="BaseResultMap" id="testCodetest" >
                    //		select * from test_Code
                    //	</select>
                    Bean bean = t.getBean();
                    List<BuildXmlBean> list=new ArrayList<>();
                    BuildXmlBean buildXmlBean=new BuildXmlBean();
                    buildXmlBean.setSql("\t\tselect * from "+bean.getDataName());
                    Map<String, String> attrs=new HashMap<>();
                    attrs.put("id",bean.getTableValue()+"test");
                    attrs.put("resultMap","BaseResultMap");
                    buildXmlBean.setAttributes(attrs);
                    buildXmlBean.setXmlElementType(XmlElementType.select);
                    list.add(buildXmlBean);

                    return list;
                };

            }

            /**
             * 自定义Controller方法
             * @return
             */
            @Override
            public BuildController BuildController() {

                //普通写法
                /*return new BuildController() {
                    @Override
                    public BuildJavaConfig custom(AutoCodeConfig autoCodeConfig) {
                        return null;
                    }
                };*/
                // lambda表达式写法
                return t->{
                    BuildJavaConfig buildJavaConfig=new BuildJavaConfig();
                    List<String> imports=new ArrayList<>();
                    imports.add("java.util.HashMap");
                    imports.add("java.util.Hashtable");
                    imports.add("java.util.Collections");
                    //自定义需要导入的类
                    buildJavaConfig.setImports(imports);

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
                    buildJavaConfig.setBuildJavaMethods(methods);


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
                    buildJavaConfig.setBuildJavaFields(fileds);

                    //自定义继承 类单继承 接口多继承
                    List<String> ex=new ArrayList<>();
                    ex.add("Object");
                    buildJavaConfig.setExtend(ex);

                    //自定义 实现 类多实现, 接口没有实现
//                    buildJavaConfig.setImplement(null);

                    return buildJavaConfig;
                };
            }

            /**
             * 自定义dao 参见controller实现.一样
             * @return
             */
            @Override
            public BuildDao BuildDao() {
                return t->null;
            }

            /**
             * 自定义service 参见controller实现.一样
             * @return
             */
            @Override
            public BuildService BuildService() {
                return t->null;
            }

            /**
             * 自定义impl 参见controller实现.一样
             * @return
             */
            @Override
            public BuildServiceImpl BuildServiceImpl() {
                return t->null;
            }

            /**
             * 自定义bean 参见controller实现.一样
             * @return
             */
            @Override
            public BuildBean BuildBean() {
                return t->null;
            }
        };

        startCode.start(StartCode.saxYaml("auto-code_simple.yaml"));
    }
}
