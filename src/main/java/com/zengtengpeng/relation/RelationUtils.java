package com.zengtengpeng.relation;

import com.zengtengpeng.autoCode.StartCode;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.config.TableConfig;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.oneToOne.BuildOneToOneController;
import com.zengtengpeng.relation.oneToOne.StartOneToOne;
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
        AutoCodeConfig autoCodeConfig = StartCode.saxYaml();
        RelationTable primary=new RelationTable();
        primary.setDataName("test_user");
        primary.setPrimaryKey("id");
        primary.setGenerate(true);
        primary.setRemark("用户");


        RelationTable foreign=new RelationTable();
        foreign.setDataName("test_class");
        foreign.setForeignKey("user_id");
        foreign.setGenerate(false);
        foreign.setExistParentPackage("com.zengtengpeng.test");
        foreign.setRemark("班级");

        StartOneToOne startOneToOne = (p, f, a) -> {

        };

        RelationUtils.oneToOne(primary,foreign,startCode,autoCodeConfig, startOneToOne);
    }


    /**
     * 一对一关系
     * @param primaryKey
     * @param foreign
     * @param autoCodeConfig
     */
    public static void oneToOne(RelationTable primaryKey, RelationTable foreign, StartCode startCode, AutoCodeConfig autoCodeConfig, StartOneToOne startOneToOne){

        if(!primaryKey.getGenerate()&& MyStringUtils.isEmpty(primaryKey.getExistParentPackage())){
            logger.info("primaryKey当generate为false时,请设置existParentPackage");
            return;
        }

        if(!foreign.getGenerate()&& MyStringUtils.isEmpty(foreign.getExistParentPackage())){
            logger.info("foreign当generate为false时,请设置existParentPackage");
            return;
        }
        List<TableConfig> tableConfigs=new ArrayList<>();
        check(primaryKey, tableConfigs,autoCodeConfig);

        check(foreign, tableConfigs,autoCodeConfig);

        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        if(tableConfigs.size()>0) {
            globalConfig.setTableNames(tableConfigs);
            startCode.start(autoCodeConfig);
        }
        startOneToOne.build(primaryKey,foreign,autoCodeConfig);

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
