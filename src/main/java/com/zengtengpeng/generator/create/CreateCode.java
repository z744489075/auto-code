package com.zengtengpeng.generator.create;

import com.zengtengpeng.generator.utils.MyStringUtils;
import com.zengtengpeng.generator.bean.AutoCodeParam;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 创建代码
 */
public interface CreateCode {


    Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);


    /**
     * 生成代码
     * @param param 参数
     */
    void startAuto( AutoCodeParam param);

    /**
     * 创建java文件
     *
     * @param fltName
     * @param param
     */
    default void createJavaFile(String fltName,AutoCodeParam param) {
        FileWriter writer = null;
        try {

            Template template = cfg.getTemplate(fltName + ".ftl");
            ArrayList<String> strings = MyStringUtils.splitByUpperCase(fltName, true);
            String c = param.getParentPack().replace(".", "/") + "/" + strings.get(0)
                    + "/";
            if(strings.size()>1){
                c+=strings.get(1);
            }
            File file = new File(param.getParentJavaPath(), c + "/");
            if (!file.exists()) {
                file.mkdirs();
            }
            writer = new FileWriter(new File(file.getPath(), param.getTableName()+ MyStringUtils.firstUpperCase(fltName) + ".java"));
            template.process(param, writer);
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    /**
     * 创建页面
     * @param ftlName
     * @param param
     */
     default void createPageFile(String ftlName, AutoCodeParam param) {
        FileWriter writer = null;
        try {
            Template template = cfg.getTemplate(ftlName +".ftl");
            File file = new File(param.getParentResourcesPath(), param.getResourcesPack()+"/"+param.getMobelName()+"/");
            if(!file.exists()){
                file.mkdirs();
            }
            writer = new FileWriter(new File(file.getPath(),param.getDataName()+"_"+ftlName.split("_")[0] + ".html"));
            template.process(param, writer);
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
