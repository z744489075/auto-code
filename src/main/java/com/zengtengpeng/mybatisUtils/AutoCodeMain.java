package com.zengtengpeng.mybatisUtils;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;


public class AutoCodeMain {
	public static void main(String[] args) throws Exception {

		List<String> dataNames= Arrays.asList("system_config","user_borrow_tender");
		create("f:/core","com.etiaolong.newYear","jdbc:mysql://localhost:3336/test_etl_bank",
				"test_etl_bank","Ty9lx_nKB4T2DO",dataNames);
	}

	/**
	 * 创建模板
	 * @param parentPath 生产代码的父路径
	 * @param parentPack 生产代码的父包
	 * @param jdbc jdbc连接
	 * @param user 数据库账号
	 * @param password 数据库密码
	 * @param dataNames 需要创建的数据库表明
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void create(String parentPath,String parentPack,String jdbc,String user,String password,List<String> dataNames) throws IOException, TemplateException {
		Map param=new HashMap();
		param.put("parentPath", parentPath);
		param.put("packParent", parentPack);
		param.put("jdbcUrl",jdbc);
		param.put("user",user);
		param.put("password",password);
		List<Map> tables=new ArrayList<>();
		File file1=new File(parentPath+"/src/main/resources");
		if (!file1.exists()){
			file1.mkdirs();
		}
		dataNames.forEach((s)->{
			Map<String,String> map=new HashMap<>();
			map.put("tableName", s);
			map.put("beanName", MybatisGlobalUtils.firstUpperCase_(s));
			tables.add(map);
		});
		param.put("tables",tables);
		freemarker.template.Configuration cfg = MreemarkerUtils.cfg;
		Template template = cfg.getTemplate("myBatisGenerator.ftl");
		File file = new File("f:/mg.xml");
		Writer out=new FileWriter(file);
		template.process(param,out);
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = null;
		try {
			config = cp.parseConfiguration(file);
			DefaultShellCallback callback = new DefaultShellCallback(overwrite);
			MyBatisGenerator myBatisGenerator = null;
			myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
			myBatisGenerator.generate(null);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}