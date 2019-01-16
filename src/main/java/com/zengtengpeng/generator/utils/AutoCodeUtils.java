package com.zengtengpeng.generator.utils;

import com.zengtengpeng.generator.bean.StartCode;
import com.zengtengpeng.generator.create.CreateCode;
import freemarker.template.Template;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoCodeUtils {

    public static CreateCode createCode;

    /**
     * 生成增删改查
     * @param startCode
     */
    public static void startByBaseCode(StartCode startCode) {
        try {
            Class.forName("com.zengtengpeng.generator.driver.BaseAutoCodeDriver");
            start(startCode);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 生成增删改查以及页面
     * @param startCode
     */
    public static void startByWebAdmin(StartCode startCode) {
        try {
            Class.forName("com.zengtengpeng.generator.driver.WebAdminAutoCodeDriver");
            start(startCode);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 开始生成代码
     */
    private static void start(StartCode startCode) {
        Map param = new HashMap();
        String parentPath = startCode.getParentPath();
        param.put("parentPath", parentPath);
        param.put("packParent", startCode.getParentPack());
        param.put("jdbcUrl", startCode.getJdbc());
        param.put("user", startCode.getUser());
        param.put("password", startCode.getPassword());
        List<Map> tables = new ArrayList<>();
        File file1 = new File(parentPath + "/src/main/resources");
        if (!file1.exists()) {
            file1.mkdirs();
        }
        startCode.getDataNames().forEach((s) -> {
            Map<String, String> map = new HashMap<>();
            map.put("tableName", s);
            map.put("beanName", MyStringUtils.firstUpperCase_(s));
            tables.add(map);
        });
        param.put("tables", tables);
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cfg.setClassLoaderForTemplateLoading(AutoCodeUtils.class.getClassLoader(),"ftl");
        try {
            Template template = cfg.getTemplate("myBatisGenerator.ftl");
            File file = new File("f:/mg.xml");
            Writer out = new FileWriter(file);
            template.process(param, out);
            List<String> warnings = new ArrayList<>();
            boolean overwrite = true;
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = null;

            config = cp.parseConfiguration(file);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = null;
            myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
