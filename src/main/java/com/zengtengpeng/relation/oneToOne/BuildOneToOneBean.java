package com.zengtengpeng.relation.oneToOne;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建一对一Bean
 */
@FunctionalInterface
public interface BuildOneToOneBean {

    Logger logger = LoggerFactory.getLogger(BuildOneToOneBean.class);

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
     * 主表导入
     * @return
     */
    default List<String> primaryImports(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        List<String> im=new ArrayList<>();
        im.add(foreign.getExistParentPackage()+"."+autoCodeConfig.getGlobalConfig().getPackageBean()+"."+
                foreign.getBeanName());
        return im;
    }

    /**
     * 主表字段
     * @return
     */
    default List<BuildJavaField> primaryFields(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return getBuildJavaFields(foreign);
    }



    /**
     * 主表方法
     * @return
     */
    default List<BuildJavaMethod> primaryMethods(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return getBuildJavaMethods(foreign);
    }

    default List<BuildJavaMethod> getBuildJavaMethods(RelationTable foreign) {
        List<BuildJavaMethod> buildJavaMethods=new ArrayList<>();

        BuildJavaMethod get=new BuildJavaMethod();
        get.setContent(String.format("return %s;",foreign.getBeanNameLower()));
        get.setRemark(foreign.getRemark());
        get.setMethodName(String.format("get%s",foreign.getBeanName()));
        get.setMethodType("public");
        get.setReturnType(foreign.getBeanName());
        buildJavaMethods.add(get);

        BuildJavaMethod set=new BuildJavaMethod();
        set.setContent(String.format("this.%s = %s;",foreign.getBeanNameLower(),foreign.getBeanNameLower()));
        set.setRemark(foreign.getRemark());
        set.setMethodName(String.format("set%s",foreign.getBeanName()));
        set.setMethodType("public");
        set.setReturnType("void");
        List<String> param=new ArrayList<>();
        param.add(foreign.getBeanName()+" "+foreign.getBeanNameLower());
        set.setParams(param);
        buildJavaMethods.add(set);

        return buildJavaMethods;
    }


    /**
     * 外表的配置
     * @return
     */
    default BuildJavaConfig customForeign(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return new BuildJavaConfig();
    }

    /**
     * 外表导入
     * @return
     */
    default List<String> foreignImports(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        List<String> im=new ArrayList<>();
        im.add(primaryKey.getExistParentPackage()+"."+autoCodeConfig.getGlobalConfig().getPackageBean()+"."+
                primaryKey.getBeanName());
        return im;
    }
    

    /**
     * 外表字段
     * @return
     */
    default List<BuildJavaField> foreignFields(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return getBuildJavaFields(primaryKey);
    }

    default List<BuildJavaField> getBuildJavaFields(RelationTable primaryKey) {
        List<BuildJavaField> buildJavaFields=new ArrayList<>();
        BuildJavaField buildJavaField=new BuildJavaField();
        buildJavaField.setRemark(primaryKey.getRemark());
        buildJavaField.setFiledName(primaryKey.getBeanNameLower());
        buildJavaField.setReturnType(primaryKey.getBeanName());
        buildJavaField.setFiledType("private");
        buildJavaFields.add(buildJavaField);
        return buildJavaFields;
    }

    /**
     * 外表方法
     * @return
     */
    default List<BuildJavaMethod> foreignMethods(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig){
        return getBuildJavaMethods(primaryKey);
    }


    /**
     * 构建主表的bean
     * @param primaryKey
     * @param autoCodeConfig
     * @param buildJavaConfig
     */
    default void buildPrimary(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){

        List<BuildJavaMethod> buildJavaMethods =buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.addAll(primaryMethods(primaryKey, foreign, autoCodeConfig));

        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(primaryImports(primaryKey,foreign,autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        buildJavaFields.addAll(primaryFields(primaryKey,foreign,autoCodeConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), primaryKey.getExistParentPackage(),
                globalConfig.getPackageBean())+"/"+primaryKey.getBeanName()+".java";
        BuildUtils.addJavaCode(filePath,buildJavaMethods,buildJavaFields,imports);
    }
    /**
     * 构建外表的bean
     * @param primaryKey
     * @param autoCodeConfig
     */
    default void buildForeign(RelationTable primaryKey, RelationTable foreign, AutoCodeConfig autoCodeConfig, BuildJavaConfig buildJavaConfig){
        List<BuildJavaMethod> buildJavaMethods = buildJavaConfig.getBuildJavaMethods();
        buildJavaMethods.addAll(foreignMethods(primaryKey, foreign, autoCodeConfig));

        List<String> imports = buildJavaConfig.getImports();
        imports.addAll(foreignImports(primaryKey,foreign,autoCodeConfig));

        List<BuildJavaField> buildJavaFields = buildJavaConfig.getBuildJavaFields();
        buildJavaFields.addAll(foreignFields(primaryKey,foreign,autoCodeConfig));

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        String filePath = BuildUtils.packageJavaPath(globalConfig.getParentPathJavaSource(), foreign.getExistParentPackage(),
                globalConfig.getPackageBean())+"/"+foreign.getBeanName()+".java";
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
        //获取受影响的文件 controller,server,serverImpl dao xml
        buildPrimary(primaryKey,foreign,autoCodeConfig,p);
        buildForeign(primaryKey,foreign,autoCodeConfig,f);

    }
}
