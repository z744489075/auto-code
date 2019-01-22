package com.zengtengpeng.generator.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zengtengpeng.common.bean.Page;
import com.zengtengpeng.generator.utils.AutoCodeUtils;
import com.zengtengpeng.generator.utils.MyStringUtils;
import com.zengtengpeng.generator.bean.AutoCodeParam;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * mybatis 自动生成代码初始化设置
 * 
 * @author zengtp
 * 
 *         2015年3月24日
 */
public class InitSetting extends PluginAdapter {



	/**
	 * xml覆盖生成
	 * @param sqlMap
	 * @param introspectedTable
	 * @return
	 */
	@Override
	public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
		try {
			java.lang.reflect.Field field = sqlMap.getClass().getDeclaredField("isMergeable");
			field.setAccessible(true);
			field.setBoolean(sqlMap, false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return true;
	}
	/**
	 * 生成bean时调用
	 */
	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		AutoCodeParam autoCodeParam=new AutoCodeParam();
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
		if(MyStringUtils.isEmpty(remarks)){
			remarks=introspectedColumn.getRemarks();
		}
		//主键
		autoCodeParam.setPrimaryKey(introspectedColumn);
		topLevelClass.addAnnotation("/**" + "\n* " + remarks
				+ "\n* @author zengtp" + "\n*/");
		topLevelClass.addImportedType("com.zengtengpeng.common.utils.DateUtils");
		topLevelClass.addImportedType("com.zengtengpeng.common.bean.Page");
		topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonIgnore");
		topLevelClass.addImportedType("org.springframework.util.StringUtils");
		//循环所以字段.如果有类型为date形式的.则增加格式化化方法 TIMESTAMP DATE
		Map cons=new HashMap();
		for (IntrospectedColumn allColumn : allColumns) {

			//日期转换
			if("TIMESTAMP".equals(allColumn.getJdbcTypeName())||"DATE".equals(allColumn.getJdbcTypeName())){
				//日期
				Method method=new Method();
				method.setName("get"+ MyStringUtils.firstUpperCase(allColumn.getJavaProperty())+"_");
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
				method.setName("get"+ MyStringUtils.firstUpperCase(allColumn.getJavaProperty())+"_");
				method.setReturnType(new FullyQualifiedJavaType("java.lang.String"));
				method.setVisibility(JavaVisibility.PUBLIC);
				method.addBodyLine("if(StringUtils.isEmpty("+allColumn.getJavaProperty()+")){");
				method.addBodyLine(" return \"\";");
				for (Map.Entry me : map.entrySet()) {
					if("name".equals(me.getKey())){
						continue;
					}
					method.addBodyLine("}else if("+allColumn.getJavaProperty()+".equals("+me.getKey()+")){");
					method.addBodyLine(" return \""+me.getValue()+"\";");
				}
				method.addBodyLine("}");
				method.addBodyLine("return \"\";");
				topLevelClass.addMethod(method);
				cons.put(allColumn.getJavaProperty(),map);
			} catch (Exception e) {
				System.out.println("字段->"+allColumn.getJavaProperty()+"注释->"+remarks1+"不是json忽略转换");
			}

		}
		autoCodeParam.setCons(cons);
		for (Method method : topLevelClass.getMethods()) {

			if(method.getName().startsWith("get")&&"Date".equalsIgnoreCase(method.getReturnType().getShortName())){
				method.addAnnotation("@JsonIgnore");
			}

		}


		//begin 生成controller,service
		JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = this.getContext().getJavaClientGeneratorConfiguration();
		String parentJavaPath=javaClientGeneratorConfiguration.getTargetProject();

		String targetPackage = javaClientGeneratorConfiguration.getTargetPackage();
		String tableName = introspectedTable.getTableConfiguration().getDomainObjectName();
		String substring = targetPackage.substring(0, targetPackage.lastIndexOf("."));
		//表注释
		autoCodeParam.setTableRemarks(remarks);
		//表列
		autoCodeParam.setAllColumns(allColumns);
		//父包路径
		autoCodeParam.setParentPack(substring);
		//javaBean 名称 AdminUser
		autoCodeParam.setTableName(tableName);
		//数据库表名称 admin_user
		autoCodeParam.setDataName(introspectedTable.getTableConfiguration().getTableName());
		//主键
		autoCodeParam.setParentJavaPath(parentJavaPath);
		autoCodeParam.setParentResourcesPath(this.getContext().getSqlMapGeneratorConfiguration().getTargetProject());
		AutoCodeUtils.createCode.startAuto(autoCodeParam);
		// end 生成controller,service
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}
	

	/**
	 * 增加方法参数
	 */
	@Override
	public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = this.getContext().getJavaModelGeneratorConfiguration();
        String targetPackage = javaModelGeneratorConfiguration.getTargetPackage();
        String tableName = introspectedTable.getTableConfiguration().getDomainObjectName();

        FullyQualifiedJavaType f=new FullyQualifiedJavaType("com.zengtengpeng.common.dao.BaseDao");
        FullyQualifiedJavaType f1=new FullyQualifiedJavaType(targetPackage+"."+tableName);
        FullyQualifiedJavaType f2=new FullyQualifiedJavaType("BaseDao<"+tableName+">");
        interfaze.addImportedType(f);
        interfaze.addImportedType(f1);
        interfaze.addSuperInterface(f2);
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
	public void initialized(IntrospectedTable introspectedTable) {
		super.initialized(introspectedTable);
	}

	/**
	 * 增加分页
	 */
	@Override
	public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		StringBuilder sb=new StringBuilder();
		sb.append("  <where>\n");
		condi(introspectedTable, sb);
		sb.append(" \t</where> \n <if test=\"orderByString!=null and orderByString!=''\"> \n\t ${orderByString} \n </if>");
		element.addElement(new TextElement(sb.toString()));
		return super.sqlMapSelectAllElementGenerated(element, introspectedTable);
	}

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
	 * 添加xml方法
	 */
	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		XmlElement parentElement = document.getRootElement();
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		String name = introspectedTable.getTableConfiguration().getDomainObjectName();
		List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
		IntrospectedColumn privateKey= primaryKeyColumns.get(0);
		introspectedTable.setSelectAllStatementId("queryByPaging");
		// 增加方法
		XmlElement el = new XmlElement("select");
		el.addAttribute(new Attribute("id", "queryByCondition"));
		el.addAttribute(new Attribute("resultMap", "BaseResultMap"));
		StringBuilder sb = new StringBuilder();
		sb.append("select ");

		for (IntrospectedColumn ic : allColumns) {
			sb.append(ic.getActualColumnName() + ",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("\n");

		sb.append(" \tfrom  "+introspectedTable.getTableConfiguration().getTableName()+" \n\t<where>\n");

		condi(introspectedTable, sb);
		sb.append(" \t</where> \n <if test=\"orderByString!=null and orderByString!=''\"> \n\t ${orderByString} \n </if>");
		el.addElement(new TextElement(sb.toString()));

		parentElement.addElement(el);
		super.sqlMapDocumentGenerated(document, introspectedTable);

		//增加方法
		XmlElement insert = new XmlElement("insert");
		insert.addAttribute(new Attribute("id", "insert"));
		insert.addAttribute(new Attribute("keyColumn", privateKey.getActualColumnName()));
		insert.addAttribute(new Attribute("keyProperty", privateKey.getJavaProperty()));
		insert.addAttribute(new Attribute("useGeneratedKeys", "true"));
		StringBuilder insertS = new StringBuilder();
		insertS.append("insert into "+introspectedTable.getTableConfiguration().getTableName()+" (");
		//CURRENT_TIMESTAMP
		List<IntrospectedColumn> collect = allColumns.stream().filter(t -> !t.isAutoIncrement()&&!"CURRENT_TIMESTAMP".equals(t.getDefaultValue())).collect(Collectors.toList());
		for (IntrospectedColumn introspectedColumn : collect) {
			insertS.append(introspectedColumn.getActualColumnName() + ",");
		}
		insertS.deleteCharAt(insertS.length() - 1);
		insertS.append(")\n values (");

		for (IntrospectedColumn introspectedColumn : collect) {
			insertS.append( " #{" + introspectedColumn.getJavaProperty() + ",jdbcType="+ introspectedColumn.getJdbcTypeName() + "},");
		}

		insertS.deleteCharAt(insertS.length() - 1);
		insertS.append(")");
		insert.addElement(new TextElement(insertS.toString()));

		parentElement.addElement(insert);
		super.sqlMapDocumentGenerated(document, introspectedTable);

		// 增加方法
		XmlElement update = new XmlElement("update");
		update.addAttribute(new Attribute("id", "update"));
		 sb = new StringBuilder();
		sb.append("update ");
		sb.append(" \t"+introspectedTable.getTableConfiguration().getTableName()+" \n\t<set>\n");
		int i=0;
		for (IntrospectedColumn introspectedColumn : collect) {
			++i;
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

		sb.append("\twhere "+privateKey.getActualColumnName()+"=#{"+privateKey.getJavaProperty()+"}");
		
		update.addElement(new TextElement(sb.toString()));
		parentElement.addElement(update);
		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}

	/**
	 * 组装条件
	 * @param introspectedTable
	 * @param sb
	 */
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
