package com.zengtengpeng.autoCode;

import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.config.TableConfig;
import com.zengtengpeng.autoCode.create.*;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.jdbc.bean.Bean;
import com.zengtengpeng.jdbc.utils.JDBCUtils;
import org.yaml.snakeyaml.Yaml;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 开始生成代码
 */
public interface StartCode {

    /**
     * 解析yaml文件
     */
    default AutoCodeConfig saxYaml(){
        Yaml yaml=new Yaml();
        AutoCodeConfig autoCodeConfig = yaml.loadAs(this.getClass().getClassLoader().getResourceAsStream("auto-code.yaml"), AutoCodeConfig.class);
       return autoCodeConfig;
    }

    /**
     * 解析数据库获得表
     * @param autoCodeConfig
     */
    default List<TableConfig> saxTable(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        List<TableConfig> tableNames = globalConfig.getTableNames();
        return tableNames;
    }

    /**
     * 构建xml
     * @return
     */
    default BuildXml BuildXml(){
        return t->null;
    }

    /**
     * 构建 controller
     * @return
     */
    default BuildController BuildController(){
        return t->null;
    }

    /**
     * 构建 dao
     * @return
     */
    default BuildDao BuildDao(){
        return t->null;
    }
    /**
     * 构建 Service
     * @return
     */
    default BuildService BuildService(){
        return t->null;
    }
    /**
     * 构建 ServiceImpl
     * @return
     */
    default BuildServiceImpl BuildServiceImpl(){
        return t->null;
    }
    /**
     * 构建 bean
     * @return
     */
    default BuildBean BuildBean(){
        return t->null;
    }


    /**
     * 解析数据库获得表
     * @param autoCodeConfig
     */
    default void build(AutoCodeConfig autoCodeConfig){
        Connection connection =null;
        try {
            connection = JDBCUtils.getConnection(autoCodeConfig.getDatasourceConfig());

            if(JDBCUtils.getTable(connection, autoCodeConfig)!=null){
                Bean bean = autoCodeConfig.getBean();
                //构建xml
                GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
                //xml
                String xml=BuildXml().build(autoCodeConfig);

                //构建 controller
                String controller=BuildController().build(autoCodeConfig);

                //构建dao
                String dao=BuildDao().build(autoCodeConfig);

                //构建server
                String server=BuildService().build(autoCodeConfig);

                //构建 serviceImpl
                String serviceImpl=BuildServiceImpl().build(autoCodeConfig);


                //构建bean
                String beans=BuildBean().build(autoCodeConfig);

                //开始生成文件
                BuildUtils.createXMLFile(globalConfig.getParentPath() + "/" + globalConfig.getResources(),globalConfig.getXmlPath(),
                        bean.getTableName()+globalConfig.getPackageDao_(),xml);

                String parentPath = globalConfig.getParentPath() + "/" + globalConfig.getJavaSource();


                BuildUtils.createJavaFile(parentPath,globalConfig.getParentPack(),globalConfig.getPackageController(),
                        bean.getTableName()+globalConfig.getPackageController_(),controller);

                BuildUtils.createJavaFile(parentPath,globalConfig.getParentPack(),globalConfig.getPackageDao(),
                        bean.getTableName()+globalConfig.getPackageDao_(),dao);

                BuildUtils.createJavaFile(parentPath,globalConfig.getParentPack(),globalConfig.getPackageService(),
                        bean.getTableName()+globalConfig.getPackageService_(),server);

                BuildUtils.createJavaFile(parentPath,globalConfig.getParentPack(),globalConfig.getPackageService()+".impl",
                        bean.getTableName()+globalConfig.getPackageService_()+"Impl",serviceImpl);

                BuildUtils.createJavaFile(parentPath,globalConfig.getParentPack(),globalConfig.getPackageBean(),
                        bean.getTableName(),beans);

                //自定义构建
                custom(autoCodeConfig);
            }
        } finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    void custom(AutoCodeConfig autoCodeConfig);

    /**
     * 开始
     * @param autoCodeConfig
     */
    default void start(AutoCodeConfig autoCodeConfig){
        if(autoCodeConfig==null){
            autoCodeConfig=saxYaml();
        }
        List<TableConfig> tableConfigs = saxTable(autoCodeConfig);
        AutoCodeConfig finalAutoCodeConfig = autoCodeConfig;
        tableConfigs.forEach(t->{
            Bean bean=new Bean();
            bean.setTableName(t.getAliasName());
            bean.setDataName(t.getDataName());
            finalAutoCodeConfig.setBean(bean);
            build(finalAutoCodeConfig);
        });
    }
}
