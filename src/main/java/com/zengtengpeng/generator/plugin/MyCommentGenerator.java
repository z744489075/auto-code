package com.zengtengpeng.generator.plugin;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.Properties;
import java.util.Set;

/**
 * 获取数据库的注释,生成javaBean的注释
 * @author zengtp
 *
 * 2015年3月24日
 */
public class MyCommentGenerator implements CommentGenerator {

	@Override
	public void addConfigurationProperties(Properties properties) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
		// TODO Auto-generated method stub
		if (introspectedColumn.getRemarks() != null && !introspectedColumn.getRemarks().equals("")) {
	        field.addJavaDocLine("/**");
	        field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
	       // addJavadocTag(field, false);
	        field.addJavaDocLine(" */");
	    }
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addGetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addJavaFileComment(CompilationUnit compilationUnit) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addComment(XmlElement xmlElement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRootComment(XmlElement rootElement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addClassAnnotation(InnerClass arg0, IntrospectedTable arg1, Set<FullyQualifiedJavaType> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFieldAnnotation(Field arg0, IntrospectedTable arg1, Set<FullyQualifiedJavaType> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFieldAnnotation(Field arg0, IntrospectedTable arg1, IntrospectedColumn arg2,
			Set<FullyQualifiedJavaType> arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addGeneralMethodAnnotation(Method arg0, IntrospectedTable arg1, Set<FullyQualifiedJavaType> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addGeneralMethodAnnotation(Method arg0, IntrospectedTable arg1, IntrospectedColumn arg2,
			Set<FullyQualifiedJavaType> arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addModelClassComment(TopLevelClass arg0, IntrospectedTable arg1) {
		// TODO Auto-generated method stub
		
	}


}
