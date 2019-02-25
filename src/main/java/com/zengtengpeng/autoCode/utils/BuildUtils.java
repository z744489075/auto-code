package com.zengtengpeng.autoCode.utils;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.generator.utils.MyStringUtils;

import java.util.List;

public class BuildUtils {
    public static void buildCustom(BuildJavaConfig custom, StringBuffer stringBuffer){
        if(custom!=null){
            //初始化字段
            List<BuildJavaField> buildJavaFields = custom.getBuildJavaFields();
            if(buildJavaFields!=null) {
                buildJavaFields.forEach(t -> {
//                StringBuffer stringBuffer = new StringBuffer();
                    List<String> annotation = t.getAnnotation();
                    if (annotation != null) {
                        annotation.forEach(tt -> {
                            stringBuffer.append("\t" + tt + "\n");
                        });
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

            //初始化方法
            List<BuildJavaMethod> buildJavaMethods = custom.getBuildJavaMethods();
            if(buildJavaMethods!=null) {
                buildJavaMethods.forEach(t -> {
                    List<String> annotation = t.getAnnotation();
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
                    stringBuffer.append(String.format("\t %s %s %s(%s)", t.getMethodType(), t.getReturnType(), t.getMethodName(), p));

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
}
