package com.zengtengpeng.relation.manyToMany;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.build.BuildBaseServiceImpl;
import com.zengtengpeng.relation.config.RelationConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建多对多serviceImpl
 */
@FunctionalInterface
public interface BuildManyToManyServiceImpl extends BuildBaseServiceImpl {

    @Override
    default BuildJavaMethod foreignSelectPrimaryAndForeign(AutoCodeConfig autoCodeConfig) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        String primaryKeyUp = relationConfig.getThirdparty().getForeignKeyUp(true);
        return select(String.format("select%sAnd%s", primary.getBeanName(), foreign.getBeanName()),autoCodeConfig, foreign,
                primary, primaryKeyUp,foreign.getForeignKeyUp(true));
    }

    @Override
    default BuildJavaMethod foreignDeletePrimaryAndForeign(AutoCodeConfig autoCodeConfig) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        String primaryKeyUp = relationConfig.getThirdparty().getForeignKeyUp(true);
        return delete(String.format("delete%sAnd%s", primary.getBeanName(), foreign.getBeanName()),autoCodeConfig,
                foreign,primary,primaryKeyUp, foreign.getForeignKeyUp(true));
    }

    /**
     * 重写insert
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod primaryInsert(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable thirdparty = relationConfig.getThirdparty();
        BuildJavaMethod buildJavaMethod=new BuildJavaMethod();
        buildJavaMethod.setRemark("多对多重写insert");
        List<String> an=new ArrayList<>();
        an.add("@Override");
        buildJavaMethod.setAnnotation(an);
        List<String> params=new ArrayList<>();
        params.add(primary.getBeanName()+" "+primary.getBeanNameLower());
        buildJavaMethod.setParams(params);
        buildJavaMethod.setMethodName("insert");
        buildJavaMethod.setMethodType("public");
        buildJavaMethod.setReturnType("int");
        StringBuffer text=new StringBuffer();
        MyStringUtils.append(text,"int insert = %s%s.insert(%s);",primary.getBeanNameLower(),globalConfig.getPackageDaoUp(),primary.getBeanNameLower());
        MyStringUtils.append(text,"String id = %s.get%s();",2,primary.getBeanNameLower(),thirdparty.getForeignKeyUp(true));
        MyStringUtils.append(text,"if(id !=null && !\"\".equals(id)){",2);
        MyStringUtils.append(text,"String[] split = id.split(\",\");",3);
        MyStringUtils.append(text,"%s%s.insertRelation(%s.get%s().toString(),split);",3,primary.getBeanNameLower(),
                globalConfig.getPackageDaoUp(),primary.getBeanNameLower(),primary.getPrimaryKeyUp(true));
        MyStringUtils.append(text,"}",2);
        MyStringUtils.append(text,"return insert;",2);
        buildJavaMethod.setContent(text.toString());
        return buildJavaMethod;
    }
    /**
     * 重写update
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod primaryUpdate(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable thirdparty = relationConfig.getThirdparty();
        BuildJavaMethod buildJavaMethod=new BuildJavaMethod();
        buildJavaMethod.setRemark("多对多重写Update");
        List<String> an=new ArrayList<>();
        an.add("@Override");
        buildJavaMethod.setAnnotation(an);
        List<String> params=new ArrayList<>();
        params.add(primary.getBeanName()+" "+primary.getBeanNameLower());
        buildJavaMethod.setParams(params);
        buildJavaMethod.setMethodName("update");
        buildJavaMethod.setMethodType("public");
        buildJavaMethod.setReturnType("int");
        StringBuffer text=new StringBuffer();
        MyStringUtils.append(text,"Integer update = %s%s.update(%s);",primary.getBeanNameLower(),globalConfig.getPackageDaoUp(),primary.getBeanNameLower());
        MyStringUtils.append(text,"String id = %s.get%s();",2,primary.getBeanNameLower(), thirdparty.getForeignKeyUp(true));
        MyStringUtils.append(text,"if(id !=null && !\"\".equals(id)){",2);
        MyStringUtils.append(text,"%s d=new %s();",3,foreign.getBeanName(),foreign.getBeanName());
        MyStringUtils.append(text,"d.set%s(%s.get%s().toString());",3,thirdparty.getPrimaryKeyUp(true),primary.getBeanNameLower(),primary.getPrimaryKeyUp(true));
        MyStringUtils.append(text,"%s%s.delete%sBy%s(d);",3,foreign.getBeanNameLower(),
                globalConfig.getPackageDaoUp(),foreign.getBeanName(),primary.getBeanName());
        MyStringUtils.append(text,"String[] split = id.split(\",\");",3);
        MyStringUtils.append(text,"%s%s.insertRelation(%s.get%s().toString(),split);",3,primary.getBeanNameLower(),
                globalConfig.getPackageDaoUp(),primary.getBeanNameLower(),primary.getPrimaryKeyUp(true));
        MyStringUtils.append(text,"}",2);
        MyStringUtils.append(text,"return update;",2);
        buildJavaMethod.setContent(text.toString());
        return buildJavaMethod;
    }
    /**
     * 外表重写insert
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod foreignInsert(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable thirdparty = relationConfig.getThirdparty();
        BuildJavaMethod buildJavaMethod=new BuildJavaMethod();
        buildJavaMethod.setRemark("多对多重写insert");
        List<String> an=new ArrayList<>();
        an.add("@Override");
        buildJavaMethod.setAnnotation(an);
        List<String> params=new ArrayList<>();
        params.add(foreign.getBeanName()+" "+foreign.getBeanNameLower());
        buildJavaMethod.setParams(params);
        buildJavaMethod.setMethodName("insert");
        buildJavaMethod.setMethodType("public");
        buildJavaMethod.setReturnType("int");
        StringBuffer text=new StringBuffer();
        MyStringUtils.append(text,"int insert = %s%s.insert(%s);",foreign.getBeanNameLower(),globalConfig.getPackageDaoUp(),foreign.getBeanNameLower());
        MyStringUtils.append(text,"String id = %s.get%s();",2,foreign.getBeanNameLower(),thirdparty.getPrimaryKeyUp(true));
        MyStringUtils.append(text,"if(id !=null && !\"\".equals(id)){",2);
        MyStringUtils.append(text,"String[] split = id.split(\",\");",3);
        MyStringUtils.append(text,"%s%s.insertRelation(%s.get%s().toString(),split);",3,foreign.getBeanNameLower(),
                globalConfig.getPackageDaoUp(),foreign.getBeanNameLower(),foreign.getForeignKeyUp(true));
        MyStringUtils.append(text,"}",2);
        MyStringUtils.append(text,"return insert;",2);

        buildJavaMethod.setContent(text.toString());
        return buildJavaMethod;
    }
    /**
     * 外表重写update
     * @param autoCodeConfig
     * @return
     */
    default BuildJavaMethod foreignUpdate(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable thirdparty = relationConfig.getThirdparty();
        BuildJavaMethod buildJavaMethod=new BuildJavaMethod();
        buildJavaMethod.setRemark("多对多重写Update");
        List<String> an=new ArrayList<>();
        an.add("@Override");
        buildJavaMethod.setAnnotation(an);
        List<String> params=new ArrayList<>();
        params.add(foreign.getBeanName()+" "+foreign.getBeanNameLower());
        buildJavaMethod.setParams(params);
        buildJavaMethod.setMethodName("update");
        buildJavaMethod.setMethodType("public");
        buildJavaMethod.setReturnType("int");
        StringBuffer text=new StringBuffer();
        MyStringUtils.append(text,"Integer update = %s%s.update(%s);",2,foreign.getBeanNameLower(),globalConfig.getPackageDaoUp(),foreign.getBeanNameLower());
        MyStringUtils.append(text,"String id = %s.get%s();",2,foreign.getBeanNameLower(),thirdparty.getPrimaryKeyUp(true));
        MyStringUtils.append(text,"if(id !=null && !\"\".equals(id)){",2);
        MyStringUtils.append(text,"%s d=new %s();",3,primary.getBeanName(),primary.getBeanName());
        MyStringUtils.append(text,"d.set%s(%s.get%s().toString());",3,thirdparty.getForeignKeyUp(true),foreign.getBeanNameLower(),foreign.getForeignKeyUp(true));
        MyStringUtils.append(text,"%s%s.delete%sBy%s(d);",3,primary.getBeanNameLower(),
                globalConfig.getPackageDaoUp(),primary.getBeanName(),foreign.getBeanName());
        MyStringUtils.append(text,"String[] split = id.split(\",\");",3);
        MyStringUtils.append(text,"%s%s.insertRelation(%s.get%s().toString(),split);",3,foreign.getBeanNameLower(),
                globalConfig.getPackageDaoUp(),foreign.getBeanNameLower(),foreign.getForeignKeyUp(true));
        MyStringUtils.append(text,"}",2);
        MyStringUtils.append(text,"return update;",2);
        buildJavaMethod.setContent(text.toString());
        return buildJavaMethod;
    }

    /**
     * 构建主表 级联查询(带分页)
     * @return
     */
    default BuildJavaMethod primarySelectPrimaryAndForeign(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        String primaryKeyUp = relationConfig.getThirdparty().getPrimaryKeyUp(true);

        return select(String.format("select%sAnd%s", primary.getBeanName(), foreign.getBeanName()),autoCodeConfig, primary,
                foreign, primaryKeyUp,primary.getPrimaryKeyUp(true));
    }

    /**
     *  根据主表id查询外表所有数据(带分页)
     * @return
     */
    default BuildJavaMethod foreignSelectForeignByPrimary(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        BuildJavaMethod buildJavaMethod=new BuildJavaMethod();
        buildJavaMethod.setRemark("根据主表id查询外表所有数据(带分页)");
        buildJavaMethod.setReturnType(foreign.getBeanName());
        buildJavaMethod.setMethodType("public");
        buildJavaMethod.setMethodName(String.format("select%sBy%s",foreign.getBeanName(),primary.getBeanName()));
        List<String> params=new ArrayList<>();
        params.add(foreign.getBeanName()+" t");
        buildJavaMethod.setParams(params);
        List<String> an=new ArrayList<>();
        an.add("@Override");
        buildJavaMethod.setAnnotation(an);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"PageHelper.startPage(t.getPage(), t.getPageSize());");
        MyStringUtils.append(content,"List<%s> lists = %s%s.select%sBy%s(t);",2,foreign.getBeanName(),foreign.getBeanNameLower(),globalConfig.getPackageDaoUp(),foreign.getBeanName(),primary.getBeanName());
        MyStringUtils.append(content,"PageInfo pageInfo = new PageInfo(lists);",2);
        MyStringUtils.append(content,"t.setRows(lists);",2);
        MyStringUtils.append(content,"t.setTotal((new Long(pageInfo.getTotal())).intValue());",2);
        MyStringUtils.append(content,"return t;",2);
        buildJavaMethod.setContent(content.toString());
        return buildJavaMethod;
    }

    /**
     *  根据外表id查询主表表所有数据(带分页)
     * @return
     */
    default BuildJavaMethod primarySelectByForeign(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        RelationConfig relationConfig = globalConfig.getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        RelationTable primary = relationConfig.getPrimary();
        BuildJavaMethod buildJavaMethod=new BuildJavaMethod();
        buildJavaMethod.setRemark("根据外表id查询主表所有数据(带分页)");
        buildJavaMethod.setReturnType(primary.getBeanName());
        buildJavaMethod.setMethodType("public");
        buildJavaMethod.setMethodName(String.format("select%sBy%s",primary.getBeanName(),foreign.getBeanName()));
        List<String> params=new ArrayList<>();
        params.add(primary.getBeanName()+" t");
        buildJavaMethod.setParams(params);
        List<String> an=new ArrayList<>();
        an.add("@Override");
        buildJavaMethod.setAnnotation(an);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"PageHelper.startPage(t.getPage(), t.getPageSize());");
        MyStringUtils.append(content,"List<%s> lists = %s%s.select%sBy%s(t);",2,primary.getBeanName(),primary.getBeanNameLower(),globalConfig.getPackageDaoUp(),primary.getBeanName(),foreign.getBeanName());
        MyStringUtils.append(content,"PageInfo pageInfo = new PageInfo(lists);",2);
        MyStringUtils.append(content,"t.setRows(lists);",2);
        MyStringUtils.append(content,"t.setTotal((new Long(pageInfo.getTotal())).intValue());",2);
        MyStringUtils.append(content,"return t;",2);
        buildJavaMethod.setContent(content.toString());
        return buildJavaMethod;
    }

    @Override
    default BuildJavaMethod primarySelectPrimaryAndForeignByCondition(AutoCodeConfig autoCodeConfig) {
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
        MyStringUtils.append(content,"%s.set%s(t.get%s().toString());",4,foreignBeanNameLower,
                relationConfig.getThirdparty().getPrimaryKeyUp(true),
                primary.getPrimaryKeyUp(true));
        MyStringUtils.append(content,"List<%s> lists=%s%s.select%sBy%s(%s);",4,foreignBeanName,foreignBeanNameLower,
                autoCodeConfig.getGlobalConfig().getPackageDaoUp(),foreignBeanName,primaryKeyBeanName,foreignBeanNameLower);
        MyStringUtils.append(content,"t.set%sList(lists);",4,foreignBeanName);
        MyStringUtils.append(content,"});",3);
        MyStringUtils.append(content,"}",2);
        MyStringUtils.append(content,"return datas;",2);
        javaMethod.setContent(content.toString());
        javaMethod.setRemark("级联条件查询 "+primary.getRemark()+"--"+foreign.getRemark());
        return javaMethod;
    }

    @Override
    default BuildJavaMethod foreignSelectPrimaryAndForeignByCondition(AutoCodeConfig autoCodeConfig) {
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
        MyStringUtils.append(content,"%s.set%s(t.get%s().toString());",4,primaryKeyBeanNameLower,relationConfig.getThirdparty().getForeignKeyUp(true),foreign.getForeignKeyUp(true));
        MyStringUtils.append(content,"List<%s> lists=%s%s.select%sBy%s(%s);",4,primaryKeyBeanName,primaryKeyBeanNameLower,
                autoCodeConfig.getGlobalConfig().getPackageDaoUp(),primaryKeyBeanName,foreignBeanName,primaryKeyBeanNameLower);
        MyStringUtils.append(content,"t.set%sList(lists);",4,primaryKeyBeanName);
        MyStringUtils.append(content,"});",3);
        MyStringUtils.append(content,"}",2);
        MyStringUtils.append(content,"return datas;",2);
        foreignSelect.setContent(content.toString());
        foreignSelect.setRemark("级联条件查询"+primary.getRemark()+"--"+foreign.getRemark());
        return foreignSelect;
    }

    default BuildJavaMethod select(String methodName, AutoCodeConfig autoCodeConfig, RelationTable primary, RelationTable foreign, String primaryKeyUp, String pId) {
        String primaryKeyBeanName_ =primary.getBeanNameLower();
        String foreignBeanName = foreign.getBeanName();
        String foreignBeanNameLower = foreign.getBeanNameLower();
        String primaryKeyBeanName = primary.getBeanName();
        BuildJavaMethod javaMethod = new BuildJavaMethod();
        List<String> an = new ArrayList<>();
        an.add("@Override");
        javaMethod.setAnnotation(an);
        javaMethod.setReturnType(primaryKeyBeanName);
        javaMethod.setMethodType("public");
        javaMethod.setMethodName(methodName);
        List<String> params=new ArrayList<>();
        params.add(primaryKeyBeanName +" "+ primaryKeyBeanName_);
        javaMethod.setParams(params);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"%s = this.selectAllByPaging(%s);",primaryKeyBeanName_,primaryKeyBeanName_);
        MyStringUtils.append(content,"if(%s!=null && %s.getRows()!=null){",2,primaryKeyBeanName_,primaryKeyBeanName_);
        MyStringUtils.append(content,"%s.getRows().forEach(t->{",3,primaryKeyBeanName_);
        MyStringUtils.append(content,"%s data= (%s) t;",4,primaryKeyBeanName,primaryKeyBeanName);
        MyStringUtils.append(content,"%s %s=new %s();",4,foreignBeanName,foreignBeanNameLower,foreignBeanName);
        MyStringUtils.append(content,"%s.set%s(data.get%s().toString());",4,foreignBeanNameLower,
                primaryKeyUp,pId);

        MyStringUtils.append(content,"List<%s> datas=%s%s.select%sBy%s(%s);",4,foreignBeanName,foreignBeanNameLower,
                autoCodeConfig.getGlobalConfig().getPackageDaoUp(),foreignBeanName,primaryKeyBeanName,foreignBeanNameLower);
        MyStringUtils.append(content,"data.set%sList(datas);",4,foreignBeanName);

        MyStringUtils.append(content,"});",3);
        MyStringUtils.append(content,"}",2);
        MyStringUtils.append(content,"return %s;",2,primaryKeyBeanName_);
        javaMethod.setContent(content.toString());
        javaMethod.setRemark("级联查询(带分页) "+primary.getRemark()+"--"+foreign.getRemark());
        return javaMethod;
    }

    /**
     * 主表删除
     * @return
     */
    @Override
    default BuildJavaMethod primaryDeletePrimaryAndForeign(AutoCodeConfig autoCodeConfig) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        String primaryKeyUp = relationConfig.getThirdparty().getPrimaryKeyUp(true);
        return delete(String.format("delete%sAnd%s", primary.getBeanName(), foreign.getBeanName()),autoCodeConfig, primary, foreign,primaryKeyUp,primary.getPrimaryKeyUp(true));
    }



    default BuildJavaMethod delete(String methodName, AutoCodeConfig autoCodeConfig, RelationTable primary,
                                   RelationTable foreign, String primaryKeyUp, String keyUp) {
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
        deleteTestUserAndTestClass.setMethodName(methodName);
        List<String> params=new ArrayList<>();
        params.add(primaryKeyBeanName +" "+ primaryKeyBeanName_);
        deleteTestUserAndTestClass.setParams(params);
        StringBuffer content=new StringBuffer();
        MyStringUtils.append(content,"%s %s=new %s();",foreignBeanName,foreignBeanNameLower,foreignBeanName);
        MyStringUtils.append(content,"%s = %s%s.selectByPrimaryKey(%s);",2,primaryKeyBeanName_,primaryKeyBeanName_,
                autoCodeConfig.getGlobalConfig().getPackageDaoUp(),primaryKeyBeanName_);
        MyStringUtils.append(content,"%s.set%s(%s.get%s().toString());",2,foreignBeanNameLower,
                primaryKeyUp,primaryKeyBeanName_,keyUp);
        MyStringUtils.append(content,"%s%s.delete%sBy%s(%s);",2,foreignBeanNameLower,autoCodeConfig.getGlobalConfig().getPackageDaoUp(),
                foreignBeanName,primaryKeyBeanName,foreignBeanNameLower);
        MyStringUtils.append(content,"return %s%s.deleteByPrimaryKey(%s);",2,primaryKeyBeanName_, globalConfig.getPackageDaoUp(),primaryKeyBeanName_);
        deleteTestUserAndTestClass.setContent(content.toString());
        deleteTestUserAndTestClass.setRemark("级联删除(根据主表删除) "+primary.getRemark()+"--"+foreign.getRemark());
        return deleteTestUserAndTestClass;
    }

    /**
     * 构建主表的serverimpl
     */
    default void buildPrimary(AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        List<BuildJavaMethod> buildJavaMethods =buildJavaConfig.getBuildJavaMethods();

        RelationTable primary = relationConfig.getPrimary();
        buildJavaMethods.add(primaryInsert(autoCodeConfig));
        buildJavaMethods.add(primaryUpdate(autoCodeConfig));
        buildJavaMethods.add(primarySelectPrimaryAndForeign(autoCodeConfig));
        buildJavaMethods.add(primarySelectPrimaryAndForeignByCondition(autoCodeConfig));
        buildJavaMethods.add(primaryDeletePrimaryAndForeign(autoCodeConfig));
        buildJavaMethods.add(primarySelectByForeign(autoCodeConfig));

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
        buildJavaMethods.add(foreignInsert(autoCodeConfig));
        buildJavaMethods.add(foreignUpdate(autoCodeConfig));
        buildJavaMethods.add(foreignSelectPrimaryAndForeign(autoCodeConfig));
        buildJavaMethods.add(foreignSelectPrimaryAndForeignByCondition(autoCodeConfig));
        buildJavaMethods.add(foreignDeletePrimaryAndForeign(autoCodeConfig));
        buildJavaMethods.add(foreignSelectForeignByPrimary(autoCodeConfig));
        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(foreignImports(autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        buildJavaFields.addAll(foreignFields(autoCodeConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), foreign.getExistParentPackage(),
                globalConfig.getPackageService()+"/impl")+"/"+foreign.getBeanName()+globalConfig.getPackageServiceUp()+"Impl"+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }
}
