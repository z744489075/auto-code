package com.zengtengpeng.autoCode;

import com.zengtengpeng.autoCode.bean.BuildXmlBean;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.config.TableConfig;
import com.zengtengpeng.autoCode.create.BuildXml;
import com.zengtengpeng.jdbc.bean.Bean;
import com.zengtengpeng.jdbc.utils.JDBCUtils;
import org.yaml.snakeyaml.Yaml;

import java.sql.Connection;
import java.sql.JDBCType;
import java.util.List;

/**
 * 初始化表数据
 */
public class InitTable {

    public static void main(String[] args) {
        Yaml yaml=new Yaml();
        System.out.println(JDBCType.valueOf(1).getName());
        AutoCodeConfig autoCodeConfig = yaml.loadAs(InitTable.class.getClassLoader().getResourceAsStream("auto-code.yaml"), AutoCodeConfig.class);
        System.out.println(autoCodeConfig);
        Connection connection = JDBCUtils.getConnection(autoCodeConfig.getDatasourceConfig());
        Bean bean=new Bean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        List<TableConfig> tableNames = globalConfig.getTableNames();

        tableNames.forEach(t->{
            bean.setTableName(t.getAliasName());
            bean.setDataName(t.getDataName());
            if(JDBCUtils.getTable(connection,bean)!=null){
                //构建xml
                autoCodeConfig.setBean(bean);
                StartBuild startBuild=new StartBuild() {
                };
                //xml
                BuildJavaConfig buildJavaConfig = autoCodeConfig.getBuildJavaConfig();
                startBuild.buildXml(autoCodeConfig);

                //构建dao
                System.out.println(startBuild.buildDao(autoCodeConfig));

                System.out.println(startBuild.buildService(autoCodeConfig));

            }
        });


    }
}
