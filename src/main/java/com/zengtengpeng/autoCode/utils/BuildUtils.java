package com.zengtengpeng.autoCode.utils;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.bean.BuildXmlBean;
import com.zengtengpeng.jdbc.bean.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BuildUtils {

    static Logger logger = LoggerFactory.getLogger(BuildUtils.class);
    /**
     * 构建字段
     * @param buildJavaFields
     * @param stringBuffer
     */
    public static void buildFields(List<BuildJavaField> buildJavaFields, StringBuffer stringBuffer){
            //初始化字段
            if(buildJavaFields!=null) {
                buildJavaFields.forEach(t -> buildFild(stringBuffer, t));
            }
    }

    public static void buildFild(StringBuffer stringBuffer, BuildJavaField t) {
        if(t!=null) {
            List<String> annotation = t.getAnnotation();
            if (!MyStringUtils.isEmpty(t.getRemark())) {
                stringBuffer.append("\t/**\n" +
                        "\t * " + t.getRemark() +
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
            stringBuffer.append(String.format("\t%s %s %s%s;\n",t.getFiledType(), t.getReturnType(), t.getFiledName(), init));
        }
    }

    /**
     * 组装sql
     * @param buildXmlBean
     * @return
     */
    public static String buildXml(BuildXmlBean buildXmlBean){
        StringBuffer sql=new StringBuffer();
        String name = buildXmlBean.getXmlElementType().name();
        sql.append("\t<"+name+" ");

        buildXmlBean.getAttributes().forEach((key, value) -> sql.append(String.format("%s=\"%s\" ", key, value)));

        MyStringUtils.append(sql,">");

        MyStringUtils.append(sql,buildXmlBean.getSql());

        MyStringUtils.append(sql,"</%s>\n",1,name);

        return sql.toString();

    }
    /**
     * 构建方法
     * @param buildJavaMethods
     * @param stringBuffer
     */
    public static void buildMethods(List<BuildJavaMethod> buildJavaMethods, StringBuffer stringBuffer){
            //初始化方法
            if(buildJavaMethods!=null) {
                buildJavaMethods.forEach(t -> buildMethod(stringBuffer, t));
            }
    }

    public static void buildMethod(StringBuffer stringBuffer, BuildJavaMethod t) {
        if(t!=null) {
            List<String> annotation = t.getAnnotation();
            if (!MyStringUtils.isEmpty(t.getRemark())) {
                stringBuffer.append("\t/**\n" +
                        "\t * " + t.getRemark() +
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
            String p = "";
            if (params.length() > 0) {
                p = params.substring(0, params.length() - 1);
            }
            stringBuffer.append(String.format("\t%s %s %s(%s)", t.getMethodType(), t.getReturnType(), t.getMethodName(), p));

            if (MyStringUtils.isEmpty(t.getContent())) {
                stringBuffer.append(";\n");
            } else {
                stringBuffer.append("{\n");
                MyStringUtils.append(stringBuffer, "%s", 2, t.getContent());
                MyStringUtils.append(stringBuffer, "}\n\n", 1);

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
     * @param cover true覆盖生成 false不覆盖
     */
    public static void createJavaFile(String parentPath, String parentPack, String pack, String beanName, String context, Boolean cover){

        File parentFile=new File(packageJavaPath(parentPath,parentPack,pack));
        if (!parentFile.exists()){
            parentFile.mkdirs();
        }

        File file=new File(parentFile,beanName+".java");
        if(file.exists()&&!cover){
            logger.info("{}已经存在,conver为{},不再重新生成",file.getAbsolutePath(),cover);
            return;
        }
        create(context, file);
    }

    /**
     * 组装路径
     * @param parentPath
     * @param parentPack
     * @param pack
     * @return
     */
    public static String packageJavaPath(String parentPath,String parentPack,String pack){
        parentPack=parentPack.replace(".","/")+"/";
        if(!MyStringUtils.isEmpty(pack)){
            pack=pack.replace(".","/")+"/";
        }
        return parentPath+"/"+parentPack+pack;
    }

    /**
     * 组装xml路径
     * @param parentPath
     * @param xmlPath
     * @return
     */
    public static File packageXmlPath(String parentPath, String xmlPath){
        File parentFile=new File(parentPath+"/"+xmlPath+"/");
        if (!parentFile.exists()){
            parentFile.mkdirs();
        }
        return parentFile;
    }
    /**
     * 创建XML文件
     * @param parentPath 根路径
     * @param xmlPath xml存放的目录
     * @param name 文件名称
     * @param context 内容
     * @param cover true 覆盖生成
     */
    public static void createXMLFile(String parentPath, String xmlPath, String name, String context, Boolean cover){
        File file=new File(packageXmlPath(parentPath,xmlPath),name+".xml");
        if(file.exists()&&!cover){
            logger.info("{}已经存在,conver为{},不再重新生成",file.getAbsolutePath(),cover);
            return;
        }
        create(context, file);
    }

    public static void create(String context, File file) {
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter=null;
        try {
            logger.info("生成路径:{}",file.getPath());
            fileOutputStream = new FileOutputStream(file);
            outputStreamWriter=new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            outputStreamWriter.write(context);
            outputStreamWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(outputStreamWriter!=null){
                    outputStreamWriter.close();
                }

                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * 追加xml代码
     */
    public static void addXmlSql(String filePath, List<BuildXmlBean> buildXmlBeans) {
        File file = new File(filePath);
        if (!file.exists()) {
            logger.error("{}->不存在,忽略追加", filePath);
            return;
        }
        FileInputStream fileInputStream=null;
        FileOutputStream fileOutputStream=null;
        OutputStreamWriter outputStreamWriter=null;
        try {
            fileInputStream=new FileInputStream(file);
            StringBuffer content=new StringBuffer();
            byte[] buff=new byte[1024];
            int len;
            while((len=fileInputStream.read(buff))!=-1){
                content.append(new String(buff,0,len, StandardCharsets.UTF_8));
            }

            //增加导入
            StringBuffer sql=new StringBuffer("\n");
            buildXmlBeans.forEach(t->{
                String str = BuildUtils.buildXml(t);
                if(content.indexOf(str)<0){
                    sql.append(str);
                }else {
                    logger.info("查找到重复,忽略生成 文件->{}\n内容->{}",file,str);
                }
            });
            content.insert(content.lastIndexOf("</mapper>")-1,sql);

            fileOutputStream=new FileOutputStream(file);
            outputStreamWriter=new OutputStreamWriter(fileOutputStream,StandardCharsets.UTF_8);
            outputStreamWriter.write(content.toString());
            outputStreamWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            colse(fileInputStream, fileOutputStream,outputStreamWriter);
        }

    }

    public static void colse(FileInputStream fileInputStream, FileOutputStream fileOutputStream,OutputStreamWriter outputStreamWriter) {
        try {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if(outputStreamWriter!=null){
                outputStreamWriter.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 追加java代码
     */
    public static void addJavaCode(String filePath, List<BuildJavaMethod> buildJavaMethods, List<BuildJavaField> buildJavaFields, List<String> imports){
        File file=new File(filePath);
        if (!file.exists()){
            logger.error("{}->不存在,忽略追加",filePath);
            return;
        }
        FileInputStream fileInputStream=null;
        FileOutputStream fileOutputStream=null;
        OutputStreamWriter outputStreamWriter=null;
        try {
            fileInputStream=new FileInputStream(file);
            StringBuffer content=new StringBuffer();
            byte[] buff=new byte[1024];
            int len;
            while((len=fileInputStream.read(buff))!=-1){
                content.append(new String(buff,0,len, StandardCharsets.UTF_8));
            }

            //增加导入
            StringBuffer im=new StringBuffer("\n");
            imports.forEach(t -> {
                String s = "import " + t + ";";
                if(content.indexOf(s)<0){
                    im.append("\n" + s);
                }else {
                    logger.info("查找到重复,忽略生成 文件{}\n内容->{}",file,s);
                }
            });
            content.insert(content.indexOf(";")+1,im);

            //增加字段
            StringBuffer filedsb=new StringBuffer("\n");
            List<BuildJavaField> fields=new ArrayList<>();

            buildJavaFields.forEach(t->{
                StringBuffer c=new StringBuffer();
                BuildUtils.buildFild(c,t);
                if(content.indexOf(c.toString())<0){
                    fields.add(t);
                }else {
                    logger.info("查找到重复,忽略生成 文件{}\n内容->{}",file,c.toString());
                }
            });
            BuildUtils.buildFields(fields,filedsb);
            content.insert(content.indexOf("{")+1,"\n"+filedsb);

            //增加方法
            StringBuffer me=new StringBuffer("\n");
            List<BuildJavaMethod> buildJavaMethod=new ArrayList<>();

            buildJavaMethods.forEach(t->{
                StringBuffer c=new StringBuffer();
                BuildUtils.buildMethod(c,t);
                if(content.indexOf(t.getMethodName()+"(")<0){
                    buildJavaMethod.add(t);
                }else {
                    logger.info("查找到重复,忽略生成 文件{}\n内容->{}",file,c.toString());
                }
            });
            BuildUtils.buildMethods(buildJavaMethod,me);
            content.insert(content.lastIndexOf("}")-1,me);
            fileOutputStream=new FileOutputStream(file);
            outputStreamWriter=new OutputStreamWriter(fileOutputStream,StandardCharsets.UTF_8);
            outputStreamWriter.write(content.toString());
            outputStreamWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            colse(fileInputStream, fileOutputStream,outputStreamWriter);
        }
    }

    /**
     * 构建where语句
     * @param bean
     * @param sql
     */
    public static void xmlWhere(Bean bean, StringBuffer sql) {

        bean.getAllColumns().forEach(t -> {
            String javaProperty = t.getBeanName();
            if ("java.lang.String".equalsIgnoreCase(t.getBeanType())) {
                MyStringUtils.append(sql, "<if test=\"%s!=null and %s!=''\"> and %s.%s = #{%s,jdbcType=%s}</if>",2,
                        javaProperty, javaProperty,bean.getDataName(), t.getJdbcName(), javaProperty, t.getJdbcType_());
            } else {
                MyStringUtils.append(sql, "<if test=\"%s!=null \"> and %s.%s = #{%s,jdbcType=%s}</if>",2,
                        javaProperty,bean.getDataName(), t.getJdbcName(), javaProperty, t.getJdbcType_());
            }

            //增加创建时间
            if (javaProperty.equals("createDate") || javaProperty.equals("createTime") || javaProperty.equals("addtime") || javaProperty.equals("addTime")) {
                MyStringUtils.append(sql, "<if test=\" startDate!=null and startDate!='' and endDate!=null and endDate!='' \"> " +
                                "and %s.%s BETWEEN #{startDate} and #{endDate}</if>",2,
                        bean.getDataName(),t.getJdbcName());
            }
        });
    }

}
