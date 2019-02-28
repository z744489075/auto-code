package com.zengtengpeng.relation.utils;

import com.zengtengpeng.autoCode.StartCode;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.config.TableConfig;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.relation.bean.RelationTable;
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
        StartCode startCode=t->{};
        RelationConfig relationConfig=new RelationConfig();

        AutoCodeConfig autoCodeConfig = StartCode.saxYaml();
        relationConfig.setAutoCodeConfig(autoCodeConfig);

        RelationTable primary=new RelationTable();
        primary.setDataName("test_user");
        primary.setPrimaryKey("id");
        primary.setGenerate(true);
        primary.setRemark("用户");
        relationConfig.setPrimary(primary);

        RelationTable foreign=new RelationTable();
        foreign.setDataName("test_class");
        foreign.setForeignKey("user_id");
        foreign.setGenerate(false);
        foreign.setExistParentPackage("com.zengtengpeng.test");
        foreign.setRemark("班级");
        relationConfig.setForeign(foreign);

        BuildOneToOne startOneToOne = rt -> {
        };

        RelationUtils.oneToOne(relationConfig,startCode, startOneToOne);
    }


    /**
     * 一对一关系
     */
    public static void oneToOne(RelationConfig relationConfig, StartCode startCode, BuildOneToOne startOneToOne){
        if (!checks(relationConfig, startCode)){
            return;
        }
        startOneToOne.build(relationConfig);
    }

    /**
     * 校验,以及生成单表
     * @param relationConfig
     * @param startCode
     * @return
     */
    public static boolean checks(RelationConfig relationConfig, StartCode startCode) {
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        AutoCodeConfig autoCodeConfig = relationConfig.getAutoCodeConfig();
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
     * 一对一关系
     */
    public static void oneToMany(RelationConfig relationConfig, StartCode startCode,BuildOneToMany buildOneToMany){
        if (!checks(relationConfig, startCode)){
            return;
        }
        buildOneToMany.build(relationConfig);
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
