package com.zengtengpeng.mybatisUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 模板工具类 2015年7月28日
 * 
 * @author zengtp
 * 
 */
public class MreemarkerUtils {
	static Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
	static {
//			System.out.println(MreemarkerUtils.class.getResource("/flts")+"--");
//			System.out.println(MreemarkerUtils.class.getClassLoader().getResource("/flts")+"--");
//			cfg.setDirectoryForTemplateLoading(new File(MreemarkerUtils.class.getResource("/").getPath(),
//					"flts"));
			cfg.setClassLoaderForTemplateLoading(MreemarkerUtils.class.getClassLoader(),"/flts");

	}

	/**
	 * 创建控制器，以及service
	 *  @param parentJavaPath
	 *            D:\workspace\bookrack\src\main\java
	 * @param param
	 *            parentPack:com.ketu.test tableName:SpotDiamond
	 * @param parentResourcesPath
	 */
	public static void createControllerAndService(String parentJavaPath, Map<String, Object> param, String parentResourcesPath) {
		createJavaFile("controller", null, parentJavaPath, param);
		createJavaFile("service", null, parentJavaPath, param);
		createJavaFile("service", "Impl", parentJavaPath, param);
	}

	private static void createJavaFile(String fltName, String child, String parentJavaPath, Map<String, Object> param) {
		FileWriter writer = null;
		try {
			if (child==null) {
				child="";
			}
			Template template = cfg.getTemplate(fltName + child + ".ftl");
			String firstUpperCase = MybatisGlobalUtils.firstUpperCase(fltName);
			File file = new File(parentJavaPath, param.get("parentPack").toString().replace(".", "/") + "/" + fltName
					+ "/" + child.toLowerCase() + "/");
			if(!file.exists()){
				file.mkdirs();
			}
			writer = new FileWriter(new File(file.getPath(),param.get("tableName")+ firstUpperCase + child + ".java"));
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
	private static void createPageFile(String fltName,String parentResourcesPath, Map<String, Object> param) {
		FileWriter writer = null;
		try {
			cfg.setDirectoryForTemplateLoading(new File(MreemarkerUtils.class.getResource("/").getPath(),
					"flts"));
			Template template = cfg.getTemplate(fltName +".ftl");
			File file = new File(parentResourcesPath, "/templates/"+param.get("mobelName").toString()+"/");
			if(!file.exists()){
				file.mkdirs();
			}
			writer = new FileWriter(new File(file.getPath(),param.get("dataName").toString()+"_"+fltName.split("_")[0] + ".html"));
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
