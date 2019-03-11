package com.zengtengpeng.relation.build;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.config.RelationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建一对一serviceImpl
 */
@FunctionalInterface
public interface BuildBaseServiceImpl {

    Logger logger = LoggerFactory.getLogger(BuildBaseServiceImpl.class);
    /**
     * 自定义构建
     * @param autoCodeConfig 关系描述配置
     * @param primaryBuildJavaConfig 主表自定义配置
     * @param foreignBuildJavaConfig 外表自定义配置
     */
    void custom(AutoCodeConfig autoCodeConfig, BuildJavaConfig primaryBuildJavaConfig, BuildJavaConfig foreignBuildJavaConfig);

    /**
     * 构建主表 级联查询(带分页)
     * @return
     */
    default BuildJavaMethod primarySelectPrimaryAndForeign(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        BuildJavaMethod javaMethod = new BuildJavaMethod();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        List<String> an = new ArrayList<>();
        an.add("@Override");
        String primaryKeyBeanName_ =primary.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String foreignBeanNameLower = foreign.getBeanNameLower();
        String primaryKeyBeanName = primary.getBeanName();
        javaMethod.setAnnotation(an);
        javaMethod.setReturnType(primaryKeyBeanName);
        javaMethod.setMethodType("public");
        javaMethod.setMethodName(String.format("select%sAnd%s", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(primaryKeyBeanName +" "+ primaryKeyBeanName_);
        javaMethod.setParams(params);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"%s = this.selectAllByPaging(%s);",primaryKeyBeanName_,primaryKeyBeanName_);
        MyStringUtils.append(content,"if(%s!=null && %s.getRows()!=null){",2,primaryKeyBeanName_,primaryKeyBeanName_);
        MyStringUtils.append(content,"%s.getRows().forEach(t->{",3,primaryKeyBeanName_);
        MyStringUtils.append(content,"%s data= (%s) t;",4,primaryKeyBeanName,primaryKeyBeanName);
        MyStringUtils.append(content,"%s %s=new %s();",4,foreignBeanName,foreignBeanNameLower,foreignBeanName);
        MyStringUtils.append(content,"%s.set%s(data.get%s());",4,foreignBeanNameLower,
                foreign.getForeignKeyUp(true),
                primary.getPrimaryKeyUp(true));
        MyStringUtils.append(content,"List<%s> lists = %s%s.selectByCondition(%s);",4,foreignBeanName,foreignBeanNameLower,autoCodeConfig.getGlobalConfig().getPackageDaoUp(),foreignBeanNameLower);
        MyStringUtils.append(content,"if(lists!=null && lists.size()>0){",4);
        MyStringUtils.append(content,"data.set%s(lists.get(0));",5,foreignBeanName);
        MyStringUtils.append(content,"}",4);
        MyStringUtils.append(content,"});",3);
        MyStringUtils.append(content,"}",2);
        MyStringUtils.append(content,"return %s;",2,primaryKeyBeanName_);
        javaMethod.setContent(content.toString());
        javaMethod.setRemark("级联查询(带分页) "+primary.getRemark()+"--"+foreign.getRemark());
        return javaMethod;
    }
    /**
     * 构建主表 级联条件查询
     * @return
     */
    default BuildJavaMethod primarySelectPrimaryAndForeignByCondition(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        BuildJavaMethod javaMethod = new BuildJavaMethod();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        List<String> an = new ArrayList<>();
        an.add("@Override");
        String primaryKeyBeanName_ =primary.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String foreignBeanNameLower = foreign.getBeanNameLower();
        String primaryKeyBeanName = primary.getBeanName();
        javaMethod.setAnnotation(an);
        javaMethod.setReturnType(String.format("List<%s>",primaryKeyBeanName) );
        javaMethod.setMethodType("public");
        javaMethod.setMethodName(String.format("select%sAnd%sByCondition", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(primaryKeyBeanName +" "+ primaryKeyBeanName_);
        javaMethod.setParams(params);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"List<%s> datas = this.selectByCondition(%s);",primaryKeyBeanName,primaryKeyBeanName_);
        MyStringUtils.append(content,"if(datas!=null){",2);
        MyStringUtils.append(content,"datas.forEach(t->{",3);
        MyStringUtils.append(content,"%s %s=new %s();",4,foreignBeanName,foreignBeanNameLower,foreignBeanName);
        MyStringUtils.append(content,"%s.set%s(t.get%s());",4,foreignBeanNameLower,
                foreign.getForeignKeyUp(true),
                primary.getPrimaryKeyUp(true));
        MyStringUtils.append(content,"List<%s> lists = %s%s.selectByCondition(%s);",4,foreignBeanName,foreignBeanNameLower,autoCodeConfig.getGlobalConfig().getPackageDaoUp(),foreignBeanNameLower);
        MyStringUtils.append(content,"if(lists!=null && lists.size()>0){",4);
        MyStringUtils.append(content,"t.set%s(lists.get(0));",5,foreignBeanName);
        MyStringUtils.append(content,"}",4);
        MyStringUtils.append(content,"});",3);
        MyStringUtils.append(content,"}",2);
        MyStringUtils.append(content,"return datas;",2);
        javaMethod.setContent(content.toString());
        javaMethod.setRemark("级联条件查询 "+primary.getRemark()+"--"+foreign.getRemark());
        return javaMethod;
    }

    /**
     * 主表级联删除(根据主表删除)
     * @return
     */
    default BuildJavaMethod primaryDeletePrimaryAndForeign(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        String primaryKeyBeanName_ = primary.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String foreignBeanNameLower = foreign.getBeanNameLower();
        String primaryKeyBeanName = primary.getBeanName();
        BuildJavaMethod deleteTestUserAndTestClass = new BuildJavaMethod();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        List<String> an = new ArrayList<>();
        an.add("@Override");
        deleteTestUserAndTestClass.setAnnotation(an);
        deleteTestUserAndTestClass.setReturnType("Integer");
        deleteTestUserAndTestClass.setMethodType("public");
        deleteTestUserAndTestClass.setMethodName(String.format("delete%sAnd%s", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(primaryKeyBeanName +" "+ primaryKeyBeanName_);
        deleteTestUserAndTestClass.setParams(params);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"%s %s=new %s();",foreignBeanName,foreignBeanNameLower,foreignBeanName);
        MyStringUtils.append(content,"%s.set%s(%s.get%s());",2,foreignBeanNameLower,foreign.getForeignKeyUp(true),primaryKeyBeanName_,primary.getPrimaryKeyUp(true));
        MyStringUtils.append(content,"%s%s.delete%sBy%s(%s);",2,foreignBeanNameLower, globalConfig.getPackageDaoUp(),foreignBeanName,primaryKeyBeanName,foreignBeanNameLower);
        MyStringUtils.append(content,"return %s%s.deleteByPrimaryKey(%s);",2,primaryKeyBeanName_, globalConfig.getPackageDaoUp(),primaryKeyBeanName_);
        deleteTestUserAndTestClass.setContent(content.toString());
        deleteTestUserAndTestClass.setRemark("级联删除(根据主表删除) "+primary.getRemark()+"--"+foreign.getRemark());
        return deleteTestUserAndTestClass;
    }

    /**
     * 主表导入
     * @return
     */
    default List<String> primaryImports(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        List<String> im=new ArrayList<>();
        im.add(foreign.getExistParentPackage()+"."+autoCodeConfig.getGlobalConfig().getPackageDao()+"."+
                foreign.getBeanName()+autoCodeConfig.getGlobalConfig().getPackageDaoUp());
        String packageBean = autoCodeConfig.getGlobalConfig().getPackageBean();
        im.add(primary.getExistParentPackage()+"."+ packageBean+"."+primary.getBeanName());
        im.add(foreign.getExistParentPackage()+"."+ packageBean+"."+foreign.getBeanName());
        im.add("java.util.List");
        im.add("com.github.pagehelper.PageHelper");
        im.add("com.github.pagehelper.PageInfo");
        return im;
    }

    /**
     * 主表字段
     * @return
     */
    default List<BuildJavaField> primaryFields(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        List<BuildJavaField> buildJavaFields=new ArrayList<>();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaField buildJavaField=new BuildJavaField();
        List<String> an=new ArrayList<>();
        an.add("@Resource");
        buildJavaField.setAnnotation(an);
        buildJavaField.setRemark(foreign.getRemark());
        String packageDao_ = autoCodeConfig.getGlobalConfig().getPackageDaoUp();
        buildJavaField.setFiledName(foreign.getBeanNameLower()+ packageDao_);
        buildJavaField.setReturnType(foreign.getBeanName()+packageDao_);
        buildJavaField.setFiledType("private");
        buildJavaFields.add(buildJavaField);
        return buildJavaFields;
    }

    /**
     * 构建外表 级联查询(带分页)
     * @return
     */
    default BuildJavaMethod foreignSelectPrimaryAndForeign(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@Override");
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        String foreignBeanNameLower = foreign.getBeanNameLower();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String primaryKeyBeanNameLower = primary.getBeanNameLower();
        foreignSelect.setAnnotation(an);
        foreignSelect.setReturnType(foreignBeanName);
        foreignSelect.setMethodType("public");
        foreignSelect.setMethodName(String.format("select%sAnd%s", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(foreignBeanName +" "+ foreignBeanNameLower);
        foreignSelect.setParams(params);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"%s = this.selectAllByPaging(%s);",foreignBeanNameLower,foreignBeanNameLower);
        MyStringUtils.append(content,"if(%s!=null && %s.getRows()!=null){",2,foreignBeanNameLower,foreignBeanNameLower);
        MyStringUtils.append(content,"%s.getRows().forEach(t->{",3,foreignBeanNameLower);
        MyStringUtils.append(content,"%s data= (%s) t;",4,foreignBeanName,foreignBeanName);
        MyStringUtils.append(content,"%s %s=new %s();",4,primaryKeyBeanName, primaryKeyBeanNameLower,primaryKeyBeanName);
        MyStringUtils.append(content,"%s.set%s(data.get%s());",4,primaryKeyBeanNameLower,primary.getPrimaryKeyUp(true),foreign.getForeignKeyUp(true));
        MyStringUtils.append(content,"data.set%s(%s%s.selectByPrimaryKey(%s));",4,primaryKeyBeanName,
                primaryKeyBeanNameLower,globalConfig.getPackageDaoUp(),primaryKeyBeanNameLower);
        MyStringUtils.append(content,"});",3);
        MyStringUtils.append(content,"}",2);
        MyStringUtils.append(content,"return %s;",2,foreign.getBeanNameLower());
        foreignSelect.setContent(content.toString());
        foreignSelect.setRemark("级联查询(带分页) "+primary.getRemark()+"--"+foreign.getRemark());
        return foreignSelect;
    }
    /**
     * 构建外表 级联条件查询
     * @return
     */
    default BuildJavaMethod foreignSelectPrimaryAndForeignByCondition(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@Override");
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primary.getBeanName();
        String foreignBeanNameLower = foreign.getBeanNameLower();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String primaryKeyBeanNameLower = primary.getBeanNameLower();
        foreignSelect.setAnnotation(an);
        foreignSelect.setReturnType(String.format("List<%s>",foreignBeanName) );
        foreignSelect.setMethodType("public");
        foreignSelect.setMethodName(String.format("select%sAnd%sByCondition", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(foreignBeanName +" "+ foreignBeanNameLower);
        foreignSelect.setParams(params);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"List<%s> datas = this.selectByCondition(%s);",foreignBeanName,foreignBeanNameLower);
        MyStringUtils.append(content,"if(datas!=null){",2);
        MyStringUtils.append(content,"datas.forEach(t->{",3);
        MyStringUtils.append(content,"%s %s=new %s();",4,primaryKeyBeanName, primaryKeyBeanNameLower,primaryKeyBeanName);
        MyStringUtils.append(content,"%s.set%s(t.get%s());",4,primaryKeyBeanNameLower,primary.getPrimaryKeyUp(true),foreign.getForeignKeyUp(true));
        MyStringUtils.append(content,"t.set%s(%s%s.selectByPrimaryKey(%s));",4,primaryKeyBeanName,
                primaryKeyBeanNameLower,globalConfig.getPackageDaoUp(),primaryKeyBeanNameLower);
        MyStringUtils.append(content,"});",3);
        MyStringUtils.append(content,"}",2);
        MyStringUtils.append(content,"return datas;",2);
        foreignSelect.setContent(content.toString());
        foreignSelect.setRemark("级联条件查询"+primary.getRemark()+"--"+foreign.getRemark());
        return foreignSelect;
    }
    /**
     * 外表级联删除(根据主表删除)
     * @return
     */
    default BuildJavaMethod foreignDeletePrimaryAndForeign(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();

        String primaryKeyBeanName_ = primary.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String foreignBeanNameLower = foreign.getBeanNameLower();
        String primaryKeyBeanName = primary.getBeanName();
        BuildJavaMethod deleteTestUserAndTestClass = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@Override");
        deleteTestUserAndTestClass.setAnnotation(an);
        deleteTestUserAndTestClass.setReturnType("Integer");
        deleteTestUserAndTestClass.setMethodType("public");
        deleteTestUserAndTestClass.setMethodName(String.format("delete%sAnd%s", primaryKeyBeanName, foreignBeanName));
        List<String> params=new ArrayList<>();
        params.add(foreignBeanName +" "+ foreignBeanNameLower);
        deleteTestUserAndTestClass.setParams(params);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"%s = %s%s.selectByPrimaryKey(%s);",foreignBeanNameLower,foreignBeanNameLower,
                globalConfig.getPackageDaoUp(),foreignBeanNameLower);
        MyStringUtils.append(content,"if(%s!=null){",2,foreignBeanNameLower);
        MyStringUtils.append(content,"%s %s=new %s();",3,primaryKeyBeanName,primaryKeyBeanName_,primaryKeyBeanName);
        MyStringUtils.append(content,"%s.set%s(%s.get%s());",3,primaryKeyBeanName_,
                primary.getPrimaryKeyUp(true),foreignBeanNameLower,foreign.getForeignKeyUp(true));
        MyStringUtils.append(content,"%s%s.deleteByPrimaryKey(%s);",3,primaryKeyBeanName_,globalConfig.getPackageDaoUp(),
                primaryKeyBeanName_);
        MyStringUtils.append(content,"}",2);
        MyStringUtils.append(content,"return %s%s.deleteByPrimaryKey(%s);",2,foreignBeanNameLower,globalConfig.getPackageDaoUp(),foreignBeanNameLower);

        deleteTestUserAndTestClass.setContent(content.toString());
        deleteTestUserAndTestClass.setRemark("级联删除(根据主表删除) "+primary.getRemark()+"--"+foreign.getRemark());
        return deleteTestUserAndTestClass;
    }


    /**
     * 外表导入
     * @return
     */
    default List<String> foreignImports(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        List<String> im=new ArrayList<>();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        im.add(primary.getExistParentPackage()+"."+autoCodeConfig.getGlobalConfig().getPackageDao()+"."+
                primary.getBeanName()+autoCodeConfig.getGlobalConfig().getPackageDaoUp());
        String packageBean = autoCodeConfig.getGlobalConfig().getPackageBean();
        im.add(primary.getExistParentPackage()+"."+ packageBean+"."+primary.getBeanName());
        im.add(foreign.getExistParentPackage()+"."+ packageBean+"."+foreign.getBeanName());
        im.add("java.util.List");
        im.add("com.github.pagehelper.PageHelper");
        im.add("com.github.pagehelper.PageInfo");
        return im;
    }


    /**
     * 外表字段
     * @return
     */
    default List<BuildJavaField> foreignFields(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        List<BuildJavaField> buildJavaFields=new ArrayList<>();
        BuildJavaField buildJavaField=new BuildJavaField();
        List<String> an=new ArrayList<>();
        an.add("@Resource");
        buildJavaField.setAnnotation(an);
        buildJavaField.setRemark(foreign.getRemark());
        String packageDao_ = autoCodeConfig.getGlobalConfig().getPackageDaoUp();
        buildJavaField.setFiledName(primary.getBeanNameLower()+ packageDao_);
        buildJavaField.setReturnType(primary.getBeanName()+packageDao_);
        buildJavaField.setFiledType("private");
        buildJavaFields.add(buildJavaField);
        return buildJavaFields;
    }


    /**
     * 构建主表的serverimpl
     */
    default void buildPrimary(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        List<BuildJavaMethod> buildJavaMethods =buildJavaConfig.getBuildJavaMethods();

        RelationTable primary = relationConfig.getPrimary();
        buildJavaMethods.add(primarySelectPrimaryAndForeign(autoCodeConfig));
        buildJavaMethods.add(primarySelectPrimaryAndForeignByCondition(autoCodeConfig));
        buildJavaMethods.add(primaryDeletePrimaryAndForeign(autoCodeConfig));

        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(primaryImports(autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        buildJavaFields.addAll(primaryFields(autoCodeConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), primary.getExistParentPackage(),
                globalConfig.getPackageService())+"/impl"+"/"+primary.getBeanName()+globalConfig.getPackageServiceUp()+"Impl"+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }
    /**
     * 构建外表的ServiceImpl
     */
    default void buildForeign(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        RelationTable foreign = relationConfig.getForeign();
        buildJavaMethods.add(foreignSelectPrimaryAndForeign(autoCodeConfig));
        buildJavaMethods.add(foreignSelectPrimaryAndForeignByCondition(autoCodeConfig));
        buildJavaMethods.add(foreignDeletePrimaryAndForeign(autoCodeConfig));
        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(foreignImports(autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        buildJavaFields.addAll(foreignFields(autoCodeConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), foreign.getExistParentPackage(),
                globalConfig.getPackageService()+"/impl")+"/"+foreign.getBeanName()+globalConfig.getPackageServiceUp()+"Impl"+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }


    /**
     * 开始构建
     */
    default void build(AutoCodeConfig autoCodeConfig){

        BuildJavaConfig p = new BuildJavaConfig();
        BuildJavaConfig f = new BuildJavaConfig();
        custom(autoCodeConfig,p,f);
        buildPrimary(autoCodeConfig,p);
        buildForeign(autoCodeConfig,f);

    }
}
