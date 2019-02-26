package com.zengtengpeng.relation;

import com.zengtengpeng.autoCode.StartCode;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.config.TableConfig;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.enums.Connect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 关系构建
 */
public class RelationBuild {

   static Logger logger = LoggerFactory.getLogger(RelationBuild.class);
    public static void main(String[] args) {
        StartCode startCode=t->{};
        RelationTable one=new RelationTable();
        one.setDataName("test_user");
        one.setPrimaryKey("id");
        one.setGenerate(true);
        one.setRemark("用户");


        RelationTable two=new RelationTable();
        two.setDataName("test_class");
        two.setForeignKey("user_id");
        two.setGenerate(false);
        two.setExistParentPackage("com.zengtengpeng.test");
        two.setRemark("班级");

        RelationBuild.build(one,two,Connect.oneToOne,startCode);
    }

    /**
     * 构建
     * @param primaryKey 主表
     * @param foreign 外键表
     * @param connect 连接关系
     * @param startCode 初始化参数
     */
    public static void build(RelationTable primaryKey, RelationTable foreign, Connect connect,StartCode startCode){

        if(!primaryKey.getGenerate()&& MyStringUtils.isEmpty(primaryKey.getExistParentPackage())){
            logger.info("primaryKey当generate为false时,请设置existParentPackage");
            return;
        }

        if(!foreign.getGenerate()&& MyStringUtils.isEmpty(foreign.getExistParentPackage())){
            logger.info("foreign当generate为false时,请设置existParentPackage");
            return;
        }
        List<TableConfig> tableConfigs=new ArrayList<>();

        check(primaryKey, tableConfigs);

        check(foreign, tableConfigs);

        AutoCodeConfig autoCodeConfig = startCode.saxYaml();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        if(tableConfigs.size()>0) {
            globalConfig.setTableNames(tableConfigs);
            startCode.start(autoCodeConfig);
        }

    }

    public static void check(RelationTable foreign, List<TableConfig> tableConfigs) {
        if(foreign.getGenerate()){
            TableConfig tableConfig=new TableConfig();
            tableConfig.setAliasName(foreign.getBeanName());
            tableConfig.setDataName(foreign.getDataName());
            tableConfigs.add(tableConfig);
        }
    }
}
