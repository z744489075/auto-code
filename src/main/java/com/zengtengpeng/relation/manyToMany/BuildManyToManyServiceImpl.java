package com.zengtengpeng.relation.manyToMany;

import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.build.BuildBaseServiceImpl;
import com.zengtengpeng.relation.config.RelationConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建一对多serviceImpl
 */
@FunctionalInterface
public interface BuildManyToManyServiceImpl extends BuildBaseServiceImpl {

    @Override
    default BuildJavaMethod foreignSelect(AutoCodeConfig autoCodeConfig) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        String primaryKeyUp = relationConfig.getThirdparty().getForeignKeyUp(true);
        return select(String.format("select%sAnd%s", primary.getBeanName(), foreign.getBeanName()),autoCodeConfig, foreign,
                primary, primaryKeyUp,foreign.getForeignKeyUp(true));
    }

    @Override
    default BuildJavaMethod foreignDelete(AutoCodeConfig autoCodeConfig) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        String primaryKeyUp = relationConfig.getThirdparty().getForeignKeyUp(true);
        return delete(String.format("delete%sAnd%s", primary.getBeanName(), foreign.getBeanName()),autoCodeConfig,
                foreign,primary,primaryKeyUp, foreign.getForeignKeyUp(true));
    }

    /**
     * 构建主表 级联查询(带分页)
     * @return
     */
    default BuildJavaMethod primarySelect(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        String primaryKeyUp = relationConfig.getThirdparty().getPrimaryKeyUp(true);

        return select(String.format("select%sAnd%s", primary.getBeanName(), foreign.getBeanName()),autoCodeConfig, primary,
                foreign, primaryKeyUp,primary.getPrimaryKeyUp(true));
    }

    @Override
    default BuildJavaMethod primarySelectByCondition(AutoCodeConfig autoCodeConfig) {
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
    default BuildJavaMethod foreignSelectByCondition(AutoCodeConfig autoCodeConfig) {
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
        //List<Clazz> datas = this.selectByCondition(clazz);
        //		if(datas!=null){
        //			datas.forEach(t->{
        //				User user=new User();
        //				user.setRoleId(t.getUserId().toString());
        //				List<User> lists=userDao.selectUserByClazz(user);
        //				t.setUserList(lists);
        //			});
        //		}
        //		return clazz;
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
    default BuildJavaMethod primaryDelete(AutoCodeConfig autoCodeConfig) {
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
}
