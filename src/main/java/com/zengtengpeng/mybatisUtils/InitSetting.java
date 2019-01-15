package com.zengtengpeng.mybatisUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zengtengpeng.common.bean.Page;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mybatis 自动生成代码初始化设置
 * 
 * @author zengtp
 * 
 *         2015年3月24日
 */
public class InitSetting extends PluginAdapter {


	Logger logger = LoggerFactory.getLogger(InitSetting.class);

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	/**
	 * 生成bean时调用
	 */
	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		topLevelClass.setSuperClass(Page.class.getName());
		topLevelClass.addImportedType(new FullyQualifiedJavaType(Page.class.getName()));
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
		IntrospectedColumn introspectedColumn=null;
		if(primaryKeyColumns!=null&&primaryKeyColumns.size()>0){
			introspectedColumn = primaryKeyColumns.get(0);
		}else{
			introspectedColumn=allColumns.get(0);
		}
		String remarks = introspectedTable.getRemarks();
		if(StringUtils.isEmpty(remarks)){
			remarks=introspectedColumn.getRemarks();
		}
		topLevelClass.addAnnotation("/**" + "\n* " + remarks
				+ "\n* @author zengtp" + "\n*/");
		topLevelClass.addImportedType("com.zengtengpeng.common.utils.DateUtils");
		topLevelClass.addImportedType("com.zengtengpeng.common.bean.Page");
		topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonIgnore");
		topLevelClass.addImportedType("org.springframework.util.StringUtils");
		//循环所以字段.如果有类型为date形式的.则增加格式化化方法 TIMESTAMP DATE
		for (IntrospectedColumn allColumn : allColumns) {

			//日期转换
			if("TIMESTAMP".equals(allColumn.getJdbcTypeName())||"DATE".equals(allColumn.getJdbcTypeName())){
				//日期
				Method method=new Method();
				method.setName("get"+ MybatisGlobalUtils.firstUpperCase(allColumn.getJavaProperty())+"_");
				method.setReturnType(new FullyQualifiedJavaType("java.lang.String"));
				method.setVisibility(JavaVisibility.PUBLIC);
				if("TIMESTAMP".equals(allColumn.getJdbcTypeName())){
					method.addBodyLine("return DateUtils.formatDateTime("+allColumn.getJavaProperty()+");");
				}else {
					method.addBodyLine("return DateUtils.formatDate("+allColumn.getJavaProperty()+");");
				}
				topLevelClass.addMethod(method);
			}

			//转换json
			ObjectMapper objectMapper=new ObjectMapper();
			String remarks1 = allColumn.getRemarks();
			try {
				Map<Object,Object> map = objectMapper.readValue(remarks1, Map.class);
				Method method=new Method();
				method.setName("get"+ MybatisGlobalUtils.firstUpperCase(allColumn.getJavaProperty())+"_");
				method.setReturnType(new FullyQualifiedJavaType("java.lang.String"));
				method.setVisibility(JavaVisibility.PUBLIC);
				method.addBodyLine("if(StringUtils.isEmpty("+allColumn.getJavaProperty()+")){");
				method.addBodyLine(" return \"\";");
				for (Map.Entry me : map.entrySet()) {
					method.addBodyLine("}else if("+allColumn.getJavaProperty()+".equals("+me.getKey()+")){");
					method.addBodyLine(" return \""+me.getValue()+"\";");
				}
				method.addBodyLine("}");
				method.addBodyLine("return \"\";");
				topLevelClass.addMethod(method);
			} catch (Exception e) {
				logger.info("字段->{}注释->{}不是json忽略转换",allColumn.getJavaProperty(),remarks1);
			}

		}

		for (Method method : topLevelClass.getMethods()) {

			if(method.getName().startsWith("get")&&"Date".equalsIgnoreCase(method.getReturnType().getShortName())){
				method.addAnnotation("@JsonIgnore");
			}

		}


		//begin 生成controller,service
		JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = this.getContext().getJavaClientGeneratorConfiguration();
		String parentJavaPath=javaClientGeneratorConfiguration.getTargetProject();
		Map<String, Object> param=new HashMap<>();
		String targetPackage = javaClientGeneratorConfiguration.getTargetPackage();
		String tableName = introspectedTable.getTableConfiguration().getDomainObjectName();
		String substring = targetPackage.substring(0, targetPackage.lastIndexOf("."));

		//表注释
		param.put("tableRemarks", remarks);
		//表列
		param.put("allColumns", allColumns);
		//父包路径
		param.put("parentPack", substring);
		//模块名称 com.etiaolong.bean  中的 etiaolong
		param.put("mobelName", substring.substring(substring.lastIndexOf(".")+1));
		//javaBean 名称 AdminUser
		param.put("tableName",tableName);
		//数据库表名称 admin_user
		param.put("dataName",introspectedTable.getTableConfiguration().getTableName());
		//javaBean 首字母小写 adminUser
		param.put("tableValue", MybatisGlobalUtils.firstLowerCase(tableName));
		//主键
		String parentResourcesPath = this.getContext().getSqlMapGeneratorConfiguration().getTargetProject();
		param.put("primaryId", MybatisGlobalUtils.firstUpperCase(introspectedColumn.getJavaProperty()));
		MreemarkerUtils.createControllerAndService(parentJavaPath, param,parentResourcesPath);
		// end 生成controller,service
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}
	

	/**
	 * 修改方法参数
	 */
	@Override
	public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		String domainObjectName = introspectedTable.getTableConfiguration().getDomainObjectName();
		method.addAnnotation(" /**" + "\n\t* 分页查询" + "\n\t* @param page 参数" + "\n\t* @return" + "\n\t*/");
		method.addParameter(new Parameter(new FullyQualifiedJavaType(domainObjectName), MybatisGlobalUtils
				.firstLowerCase(domainObjectName)));

		// 增加dao方法
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(new FullyQualifiedJavaType(domainObjectName), MybatisGlobalUtils
				.firstLowerCase(domainObjectName)));
		FullyQualifiedJavaType javaType=new FullyQualifiedJavaType(List.class.getName());
		javaType.addTypeArgument(new FullyQualifiedJavaType(domainObjectName));
		interfaze.addMethod(addMethod(method, introspectedTable, "根据条件查询", "query" + domainObjectName + "ByCondition",
				javaType, parameters));

		 super.clientSelectAllMethodGenerated(method, interfaze, introspectedTable);
		// 增加dao方法
		parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(new FullyQualifiedJavaType(domainObjectName), MybatisGlobalUtils
				.firstLowerCase(domainObjectName)));
		javaType=new FullyQualifiedJavaType(Integer.class.getName());
		interfaze.addMethod(addMethod(method, introspectedTable, "更新(忽略null)", "update" + domainObjectName + "ByKeyWithNotNull",
				javaType, parameters));

		return super.clientSelectAllMethodGenerated(method, interfaze, introspectedTable);
	}

	/**
	 * 此方法用于实现接口
	 */
	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		/*
		 * FullyQualifiedJavaType superInterface=new
		 * FullyQualifiedJavaType(PagingDao.class.getName());
		 * interfaze.addSuperInterface(superInterface);
		 */
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}

	/**
	 * 增加分页
	 */
	@Override
	public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		/*XmlElement head = new XmlElement("include"); //$NON-NLS-1$
		head.addAttribute(new Attribute("refid", "paging.head")); //$NON-NLS-1$ //$NON-NLS-2$
		element.addElement(0, head);
		XmlElement end = new XmlElement("include"); //$NON-NLS-1$
		end.addAttribute(new Attribute("refid", "paging.tail")); //$NON-NLS-1$ //$NON-NLS-2$
		element.addElement(end);*/
		StringBuilder sb=new StringBuilder();
		sb.append("  <where>\n");
		condi(introspectedTable, sb);
		sb.append(" \t</where> \n <if test=\"orderByString!=null and orderByString!=''\"> \n\t ${orderByString} \n </if>");
		element.addElement(new TextElement(sb.toString()));
		return super.sqlMapSelectAllElementGenerated(element, introspectedTable);
	}





	/**
	 * 添加xml方法
	 */
	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		XmlElement parentElement = document.getRootElement();
		String name = introspectedTable.getTableConfiguration().getDomainObjectName();
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		introspectedTable.setSelectAllStatementId("query" + name + "ByPaging");
		// 产生分页语句前半部分
		XmlElement el = new XmlElement("select");
		el.addAttribute(new Attribute("id", "query" + name + "ByCondition"));
		el.addAttribute(new Attribute("resultMap", "BaseResultMap"));
		StringBuilder sb = new StringBuilder();
		sb.append("select ");

		for (IntrospectedColumn introspectedColumn : allColumns) {
			sb.append(introspectedColumn.getActualColumnName() + ",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("\n");

		sb.append(" \tfrom  "+introspectedTable.getTableConfiguration().getTableName()+" \n\t<where>\n");

		condi(introspectedTable, sb);
		sb.append(" \t</where> \n <if test=\"orderByString!=null and orderByString!=''\"> \n\t ${orderByString} \n </if>");
		el.addElement(new TextElement(sb.toString()));

		parentElement.addElement(el);
		 super.sqlMapDocumentGenerated(document, introspectedTable);
		// 产生分页语句前半部分
		XmlElement update = new XmlElement("update");
		update.addAttribute(new Attribute("id", "update" + name + "ByKeyWithNotNull"));
		 sb = new StringBuilder();
		sb.append("update ");
		sb.append(" \t"+introspectedTable.getTableConfiguration().getTableName()+" \n\t<set>\n");
		int i=0;
		for (IntrospectedColumn introspectedColumn : allColumns) {
			if(++i==1){
				continue;
			}
			String javaProperty = introspectedColumn.getJavaProperty();
			String actualColumnName = introspectedColumn.getActualColumnName();
			sb.append("\t\t<if test=\"" + javaProperty + "!=null ");
			
			if (introspectedColumn.getFullyQualifiedJavaType().getShortName().equals("String")) {
				sb.append("and " + javaProperty + "!=''");
			}
			
			sb.append("\">");
			sb.append(actualColumnName + " = #{" + javaProperty + ",jdbcType="
					+ introspectedColumn.getJdbcTypeName() + "}");
			if (i<allColumns.size()) {
				sb.append(",");
			}
			sb.append(" </if>\n");
		}
		sb.append(" \t</set>\n");
		sb.append("\twhere id=#{id}");
		
		update.addElement(new TextElement(sb.toString()));
		parentElement.addElement(update);
		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}

	private void condi(IntrospectedTable introspectedTable, StringBuilder sb) {
		Boolean b=true;
		for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
			String javaProperty = introspectedColumn.getJavaProperty();
			String actualColumnName = introspectedColumn.getActualColumnName();
			if(b&&(javaProperty.equals("createDate")||javaProperty.equals("createTime")||javaProperty.equals("addtime")||javaProperty.equals("addTime"))) {
				b=!b;
				sb.append("\t\t<if test=\" startDate!=null and startDate!='' and endDate!=null and endDate!='' ");

				sb.append("\">");
				if("TIMESTAMP".equals(introspectedColumn.getJdbcTypeName())){
					sb.append(" and " + actualColumnName + " BETWEEN #{startDate} and #{endDate}</if>\n");
				}else {
					sb.append(" and left(" + actualColumnName + ",10) BETWEEN left(#{startDate},10) and left(#{endDate},10)</if>\n");
				}
			}else {
				sb.append("\t\t<if test=\"" + javaProperty + "!=null ");

				if (introspectedColumn.getFullyQualifiedJavaType().getShortName().equals("String")) {
					sb.append("and " + javaProperty + "!=''");
				}
				sb.append("\">");
				sb.append(" and " + actualColumnName + " = #{" + javaProperty + ",jdbcType="
						+ introspectedColumn.getJdbcTypeName() + "}</if>\n");
			}
		}
	}

	/**
	 * 增加dao方法
	 * 
	 * @param method
	 * @param introspectedTable
	 * @return
	 */
	private Method addMethod(Method method, IntrospectedTable introspectedTable, String annotationValue,
			String methodName, FullyQualifiedJavaType returnType, List<Parameter> parameters) {

		String domainObjectName = introspectedTable.getTableConfiguration().getDomainObjectName();
		Method m = new Method(methodName);

		m.setVisibility(method.getVisibility());
		m.addAnnotation("/**" + "\n\t* " + annotationValue + "\n\t*/");
		m.setReturnType(returnType);
		

		for (Parameter parameter : parameters) {

			m.addParameter(parameter);
		}

		context.getCommentGenerator().addGeneralMethodComment(m, introspectedTable);
		return m;
	}

	@Override
	public boolean validate(List<String> warnings) {
		// TODO Auto-generated method stub
		return true;
	}

}
