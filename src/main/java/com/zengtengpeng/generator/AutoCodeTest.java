package com.zengtengpeng.generator;

import com.zengtengpeng.generator.bean.StartCode;
import com.zengtengpeng.generator.utils.AutoCodeUtils;
import com.zengtengpeng.generator.utils.MyStringUtils;
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


public class AutoCodeTest {
	public static void main(String[] args) {

		List<String> dataNames= Arrays.asList("test_auto_code2");
		StartCode startCode=new StartCode();
		startCode.setJdbc("jdbc:mysql://localhost:3306/auto_code");
		startCode.setUser("root");
		startCode.setPassword("111111");
		startCode.setDataNames(dataNames);
		startCode.setParentPath("f:/core");
		startCode.setParentPack("com.etiaolong.newYear");
		AutoCodeUtils.startByWebAdmin(startCode);
	}

}