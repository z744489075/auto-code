package com.zengtengpeng.autoCode.utils;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class BuildUtils {
    /**
     * 构建字段
     * @param buildJavaConfig
     * @param stringBuffer
     */
    public static void buildField(BuildJavaConfig buildJavaConfig, StringBuffer stringBuffer){
        if(buildJavaConfig!=null){
            //初始化字段
            List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
            if(buildJavaFields!=null) {
                buildJavaFields.forEach(t -> {
//                StringBuffer stringBuffer = new StringBuffer();
                    List<String> annotation = t.getAnnotation();
                    if (!MyStringUtils.isEmpty(t.getRemark())){
                        stringBuffer.append("\t/**\n" +
                                "\t * "+t.getRemark()+
                                "\n\t */\n");
                    }
                    if (annotation != null) {
                        annotation.forEach(tt -> stringBuffer.append("\t" + tt + "\n"));
                    }
                    String init = t.getInit();
                    if (!MyStringUtils.isEmpty(init)) {
                        init = "=" + init;
                    } else {
                        init = "";
                    }
                    stringBuffer.append(String.format("\t%s %s %s;\n\n", t.getReturnType(), t.getFiledName(), init));
                });
            }

        }
    }

    /**
     * 构建方法
     * @param buildJavaConfig
     * @param stringBuffer
     */
    public static void buildMethods(BuildJavaConfig buildJavaConfig, StringBuffer stringBuffer){
        if(buildJavaConfig!=null){
            //初始化方法
            List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
            if(buildJavaMethods!=null) {
                buildJavaMethods.forEach(t -> {
                    List<String> annotation = t.getAnnotation();
                    if (!MyStringUtils.isEmpty(t.getRemark())){
                        stringBuffer.append("\t/**\n" +
                                "\t * "+t.getRemark()+
                                "\n\t */\n");
                    }
                    if (annotation != null) {
                        annotation.forEach(ttt -> MyStringUtils.append(stringBuffer, "%s", 1, ttt));
                    }
                    StringBuffer params = new StringBuffer();
                    List<String> params1 = t.getParams();
                    if (params1 != null) {
                        params1.forEach(tt -> params.append(tt + ","));
                    }
                    String p="";
                    if(params.length()>0){
                        p=params.substring(0, params.length() - 1);
                    }
                    stringBuffer.append(String.format("\t%s %s %s(%s)", t.getMethodType(), t.getReturnType(), t.getMethodName(), p));

                    if (MyStringUtils.isEmpty(t.getContent())) {
                        stringBuffer.append(";\n");
                    } else {
                        stringBuffer.append("{\n");
                        MyStringUtils.append(stringBuffer, "%s", 2, t.getContent());
                        MyStringUtils.append(stringBuffer, "}\n\n", 1);

                    }
                });
            }
        }
    }

    /**
     * 创建java文件
     * @param parentPath 根路径
     * @param parentPack 父包
     * @param pack 包名
     * @param beanName 文件名称
     * @param context 内容
     */
    public static void createJavaFile(String parentPath,String parentPack,String pack,String beanName,String context){
        parentPack=parentPack.replace(".","/")+"/";
        if(!MyStringUtils.isEmpty(pack)){
            pack=pack.replace(".","/")+"/";
        }
        File parentFile=new File(parentPath+"/"+parentPack+pack);
        if (!parentFile.exists()){
            parentFile.mkdirs();
        }

        File file=new File(parentFile,beanName+".java");
        create(context, file);
    }


    /**
     * 创建XML文件
     * @param parentPath 根路径
     * @param xmlPath xml存放的目录
     * @param name 文件名称
     * @param context 内容
     */
    public static void createXMLFile(String parentPath,String xmlPath,String name,String context){

        File parentFile=new File(parentPath+"/"+xmlPath);
        if (!parentFile.exists()){
            parentFile.mkdirs();
        }

        File file=new File(parentFile,name+".xml");
        create(context, file);
    }

    public static void create(String context, File file) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(context.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
