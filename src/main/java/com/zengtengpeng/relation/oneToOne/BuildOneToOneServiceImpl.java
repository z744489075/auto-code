package com.zengtengpeng.relation.oneToOne;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建一对一serviceImpl
 */
@FunctionalInterface
public interface BuildOneToOneServiceImpl {

    Logger logger = LoggerFactory.getLogger(BuildOneToOneServiceImpl.class);
    /**
     * 自定义构建
     * @param primary 主表自定义
     * @param foreign 外表自定义
     */
    void custom(BuildJavaConfig primary,BuildJavaConfig foreign);
    /**
     * 主表的配置
     * @return
     */
    default BuildJavaConfig customPrimary(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return new BuildJavaConfig();
    }




    /**
     * 构建主表 级联查询(带分页)
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod primarySelect(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        BuildJavaMethod javaMethod = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@Override");
        String primaryKeyBeanName_ =primaryKey.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String foreignBeanNameLower = foreign.getBeanNameLower();
        String primaryKeyBeanName = primaryKey.getBeanName();
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
                primaryKey.getPrimaryKeyUp(true));
        MyStringUtils.append(content,"List<%s> lists = %s%s.selectByCondition(%s);",4,foreignBeanName,foreignBeanNameLower,autoCodeConfig.getGlobalConfig().getPackageDaoUp(),foreignBeanNameLower);
        MyStringUtils.append(content,"if(lists!=null && lists.size()>0){",4);
        MyStringUtils.append(content,"data.set%s(lists.get(0));",5,foreignBeanName);
        MyStringUtils.append(content,"}",4);
        MyStringUtils.append(content,"});",3);
        MyStringUtils.append(content,"}",2);
        MyStringUtils.append(content,"return %s;",2,primaryKeyBeanName_);
        javaMethod.setContent(content.toString());
        javaMethod.setRemark("级联查询(带分页) "+primaryKey.getRemark()+"--"+foreign.getRemark());
        return javaMethod;
    }

    /**
     * 主表级联删除(根据主表删除)
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod primaryDelete(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        String primaryKeyBeanName_ = primaryKey.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String foreignBeanNameLower = foreign.getBeanNameLower();
        String primaryKeyBeanName = primaryKey.getBeanName();
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
        MyStringUtils.append(content,"%s.set%s(%s.get%s());",2,foreignBeanNameLower,foreign.getForeignKeyUp(true),primaryKeyBeanName_,primaryKey.getPrimaryKeyUp(true));
        MyStringUtils.append(content,"%s%s.deleteBy%s(%s);",2,foreignBeanNameLower, globalConfig.getPackageDaoUp(),foreign.getForeignKeyUp(true),foreignBeanNameLower);
        MyStringUtils.append(content,"return %s%s.deleteByPrimaryKey(%s);",2,primaryKeyBeanName_, globalConfig.getPackageDaoUp(),primaryKeyBeanName_);
        deleteTestUserAndTestClass.setContent(content.toString());
        deleteTestUserAndTestClass.setRemark("级联删除(根据主表删除) "+primaryKey.getRemark()+"--"+foreign.getRemark());
        return deleteTestUserAndTestClass;
    }

    /**
     * 主表导入
     * @return
     */
    default List<String> primaryImports(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
//        import com.zengtengpeng.test.dao.TestClassDao;
        List<String> im=new ArrayList<>();
        im.add(foreign.getExistParentPackage()+"."+autoCodeConfig.getGlobalConfig().getPackageDao()+"."+
                foreign.getBeanName()+autoCodeConfig.getGlobalConfig().getPackageDaoUp());
        String packageBean = autoCodeConfig.getGlobalConfig().getPackageBean();
        im.add(primaryKey.getExistParentPackage()+"."+ packageBean+"."+primaryKey.getBeanName());
        im.add(foreign.getExistParentPackage()+"."+ packageBean+"."+foreign.getBeanName());
        im.add("java.util.List");
        return im;
    }

    /**
     * 主表字段
     * @return
     */
    default List<BuildJavaField> primaryFields(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        List<BuildJavaField> buildJavaFields=new ArrayList<>();
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
     * 外表的配置
     * @return
     */
    default BuildJavaConfig customForeign(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return new BuildJavaConfig();
    }


    /**
     * 构建外表 级联查询(带分页)
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod foreignSelect(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        BuildJavaMethod foreignSelect = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@Override");
        String foreignBeanName = foreign.getBeanName();
        String primaryKeyBeanName = primaryKey.getBeanName();
        String foreignBeanNameLower = foreign.getBeanNameLower();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String primaryKeyBeanNameLower = primaryKey.getBeanNameLower();
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
        MyStringUtils.append(content,"%s.set%s(data.get%s());",4,primaryKeyBeanNameLower,primaryKey.getPrimaryKeyUp(true),foreign.getForeignKeyUp(true));
        MyStringUtils.append(content,"data.set%s(%s%s.selectByPrimaryKey(%s));",4,primaryKeyBeanName,
                primaryKeyBeanNameLower,globalConfig.getPackageDaoUp(),primaryKeyBeanNameLower);
        MyStringUtils.append(content,"});",3);
        MyStringUtils.append(content,"}",2);
        MyStringUtils.append(content,"return %s;",2,foreign.getBeanNameLower());
        foreignSelect.setContent(content.toString());
        foreignSelect.setRemark("级联查询(带分页) "+primaryKey.getRemark()+"--"+foreign.getRemark());
        return foreignSelect;
    }
    /**
     * 外表级联删除(根据主表删除)
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod foreignDelete(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();

        String primaryKeyBeanName_ = primaryKey.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String foreignBeanNameLower = foreign.getBeanNameLower();
        String primaryKeyBeanName = primaryKey.getBeanName();
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
                primaryKey.getPrimaryKeyUp(true),foreignBeanNameLower,foreign.getForeignKeyUp(true));
        MyStringUtils.append(content,"%s%s.deleteByPrimaryKey(%s);",3,primaryKeyBeanName_,globalConfig.getPackageDaoUp(),
                primaryKeyBeanName_);
        MyStringUtils.append(content,"}",2);
        MyStringUtils.append(content,"return %s%s.deleteByPrimaryKey(%s);",2,foreignBeanNameLower,globalConfig.getPackageDaoUp(),foreignBeanNameLower);

        deleteTestUserAndTestClass.setContent(content.toString());
        deleteTestUserAndTestClass.setRemark("级联删除(根据主表删除) "+primaryKey.getRemark()+"--"+foreign.getRemark());
        return deleteTestUserAndTestClass;
    }


    /**
     * 外表导入
     * @return
     */
    default List<String> foreignImports(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        List<String> im=new ArrayList<>();
        im.add(primaryKey.getExistParentPackage()+"."+autoCodeConfig.getGlobalConfig().getPackageDao()+"."+
                primaryKey.getBeanName()+autoCodeConfig.getGlobalConfig().getPackageDaoUp());
        String packageBean = autoCodeConfig.getGlobalConfig().getPackageBean();
        globalImports(primaryKey, foreign, im, packageBean);
        return im;
    }

    /**
     * 公共的参数
     * @param primaryKey
     * @param foreign
     * @param im
     * @param packageBean
     */
    default void globalImports(RelationTable primaryKey, RelationTable foreign, List<String> im, String packageBean) {
        im.add(primaryKey.getExistParentPackage()+"."+ packageBean+"."+primaryKey.getBeanName());
        im.add(foreign.getExistParentPackage()+"."+ packageBean+"."+foreign.getBeanName());
    }


    /**
     * 外表字段
     * @return
     */
    default List<BuildJavaField> foreignFields(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        List<BuildJavaField> buildJavaFields=new ArrayList<>();
        BuildJavaField buildJavaField=new BuildJavaField();
        List<String> an=new ArrayList<>();
        an.add("@Resource");
        buildJavaField.setAnnotation(an);
        buildJavaField.setRemark(foreign.getRemark());
        String packageDao_ = autoCodeConfig.getGlobalConfig().getPackageDaoUp();
        buildJavaField.setFiledName(primaryKey.getBeanNameLower()+ packageDao_);
        buildJavaField.setReturnType(primaryKey.getBeanName()+packageDao_);
        buildJavaField.setFiledType("private");
        buildJavaFields.add(buildJavaField);
        return buildJavaFields;
    }


    /**
     * 构建主表的serverimpl
     * @param primaryKey
     * @param autoCodeConfig
     */
    default void buildPrimary(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        List<BuildJavaMethod> buildJavaMethods =buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.add(primarySelect(primaryKey,foreign,autoCodeConfig));
        buildJavaMethods.add(primaryDelete(primaryKey,foreign,autoCodeConfig));

        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(primaryImports(primaryKey,foreign,autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        buildJavaFields.addAll(primaryFields(primaryKey,foreign,autoCodeConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), primaryKey.getExistParentPackage(),
                globalConfig.getPackageService())+"/impl"+"/"+primaryKey.getBeanName()+globalConfig.getPackageServiceUp()+"Impl"+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }
    /**
     * 构建外表的ServiceImpl
     * @param primaryKey
     * @param autoCodeConfig
     */
    default void buildForeign(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.add(foreignSelect(primaryKey,foreign,autoCodeConfig));
        buildJavaMethods.add(foreignDelete(primaryKey,foreign,autoCodeConfig));
        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(foreignImports(primaryKey,foreign,autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        buildJavaFields.addAll(foreignFields(primaryKey,foreign,autoCodeConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), foreign.getExistParentPackage(),
                globalConfig.getPackageService()+"/impl")+"/"+foreign.getBeanName()+globalConfig.getPackageServiceUp()+"Impl"+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }


    /**
     * 开始构建
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     */
    default void build(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        BuildJavaConfig p = customPrimary(primaryKey, foreign, autoCodeConfig);

        BuildJavaConfig f = customForeign(primaryKey, foreign, autoCodeConfig);
        custom(p,f);
        buildPrimary(primaryKey,foreign,autoCodeConfig,p);
        buildForeign(primaryKey,foreign,autoCodeConfig,f);

    }
}
