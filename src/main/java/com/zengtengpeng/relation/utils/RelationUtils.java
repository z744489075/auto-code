package com.zengtengpeng.relation.utils;

import com.zengtengpeng.autoCode.StartCode;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.config.TableConfig;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.manyToMany.BuildManyToMany;
import com.zengtengpeng.relation.oneToMany.BuildOneToMany;
import com.zengtengpeng.relation.oneToOne.BuildOneToOne;
import com.zengtengpeng.relation.config.RelationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 关系构建工具类
 */
public class RelationUtils {

   static Logger logger = LoggerFactory.getLogger(RelationUtils.class);
    public static void main(String[] args) {

        RelationUtils.manyToMany(StartCode.saxYaml(),t->{}, c -> {});
        RelationUtils.oneToMany(StartCode.saxYaml(),t->{}, c -> {});
        RelationUtils.oneToOne(StartCode.saxYaml(),t->{}, c -> {});
    }


    /**
     * 一对一关系
     */
    public static void oneToOne(AutoCodeConfig autoCodeConfig, StartCode startCode, BuildOneToOne startOneToOne){
        if (!checks(autoCodeConfig, startCode)){
            return;
        }
        startOneToOne.build(autoCodeConfig);
    }

    /**
     * 校验,以及生成单表
     * @param startCode
     * @return
     */
    public static boolean checks(AutoCodeConfig autoCodeConfig, StartCode startCode) {
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        if (!primary.getGenerate() && MyStringUtils.isEmpty(primary.getExistParentPackage())) {
            logger.info("primaryKey当generate为false时,请设置existParentPackage");
            return false;
        }

        if (!foreign.getGenerate() && MyStringUtils.isEmpty(foreign.getExistParentPackage())) {
            logger.info("foreign当generate为false时,请设置existParentPackage");
            return false;
        }
        List<TableConfig> tableConfigs = new ArrayList<>();
        check(primary, tableConfigs, autoCodeConfig);

        check(foreign, tableConfigs, autoCodeConfig);

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        if (tableConfigs.size() > 0) {
            globalConfig.setTableNames(tableConfigs);
            startCode.start(autoCodeConfig);
        }
        return true;
    }

    /**
     * 一对多关系
     */
    public static void oneToMany(AutoCodeConfig autoCodeConfig, StartCode startCode,BuildOneToMany buildOneToMany){
        if (!checks(autoCodeConfig, startCode)){
            return;
        }
        buildOneToMany.build(autoCodeConfig);
    }
    /**
     * 多对多关系
     */
    public static void manyToMany(AutoCodeConfig autoCodeConfig, StartCode startCode, BuildManyToMany buildManyToMany){
        if (!checks(autoCodeConfig, startCode)){
            return;
        }
        RelationTable thirdparty = autoCodeConfig.getGlobalConfig().getRelationConfig().getThirdparty();
        if(thirdparty==null || MyStringUtils.isEmpty(thirdparty.getDataName())
                || MyStringUtils.isEmpty(thirdparty.getPrimaryKey()) || MyStringUtils.isEmpty(thirdparty.getForeignKey())){
            logger.info("多对多请将第三表信息填写完整");
            return;
        }

        buildManyToMany.build(autoCodeConfig);
    }


    public static void check(RelationTable relationTable, List<TableConfig> tableConfigs,AutoCodeConfig autoCodeConfig) {
        if(relationTable.getGenerate()){
            relationTable.setExistParentPackage(autoCodeConfig.getGlobalConfig().getParentPack());
            TableConfig tableConfig=new TableConfig();
            tableConfig.setAliasName(relationTable.getBeanName());
            tableConfig.setDataName(relationTable.getDataName());
            tableConfigs.add(tableConfig);
        }
    }
}
