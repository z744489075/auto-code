package com.zengtengpeng.autoCode;

import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.config.TableConfig;
import com.zengtengpeng.autoCode.create.*;
import com.zengtengpeng.autoCode.utils.BuildUtils;
import com.zengtengpeng.jdbc.bean.Bean;
import com.zengtengpeng.jdbc.utils.JDBCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 开始生成代码
 */
@FunctionalInterface
public interface StartCode {

    Logger logger = LoggerFactory.getLogger(StartCode.class);
    /**
     * 解析yaml文件
     */
    static AutoCodeConfig saxYaml(){
        Yaml yaml=new Yaml();
        return yaml.loadAs(StartCode.class.getClassLoader().getResourceAsStream("auto-code.yaml"), AutoCodeConfig.class);
    }

    /**
     * 解析数据库获得表
     * @param autoCodeConfig
     */
    default List<TableConfig> saxTable(AutoCodeConfig autoCodeConfig){
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        return globalConfig.getTableNames();
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

    default Connection getConnection(AutoCodeConfig autoCodeConfig){
        return JDBCUtils.getConnection(autoCodeConfig.getDatasourceConfig());
    }


    /**
     * 开始获取表数据.生成文件
     * @param autoCodeConfig
     */
    default void build(AutoCodeConfig autoCodeConfig){
        Connection connection =null;
        try {
            connection =getConnection(autoCodeConfig);

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
                Boolean cover = globalConfig.getCover();
                BuildUtils.createXMLFile(globalConfig.getParentPathResources() ,globalConfig.getXmlPath(),
                        bean.getTableName()+globalConfig.getPackageDaoUp(),xml, cover);

                String parentPath = globalConfig.getParentPathJavaSource();


                BuildUtils.createJavaFile(parentPath,globalConfig.getParentPack(),globalConfig.getPackageController(),
                        bean.getTableName()+globalConfig.getPackageControllerUp(),controller,cover);

                BuildUtils.createJavaFile(parentPath,globalConfig.getParentPack(),globalConfig.getPackageDao(),
                        bean.getTableName()+globalConfig.getPackageDaoUp(),dao, cover);

                BuildUtils.createJavaFile(parentPath,globalConfig.getParentPack(),globalConfig.getPackageService(),
                        bean.getTableName()+globalConfig.getPackageServiceUp(),server, cover);

                BuildUtils.createJavaFile(parentPath,globalConfig.getParentPack(),globalConfig.getPackageService()+".impl",
                        bean.getTableName()+globalConfig.getPackageServiceUp()+"Impl",serviceImpl, cover);

                BuildUtils.createJavaFile(parentPath,globalConfig.getParentPack(),globalConfig.getPackageBean(),
                        bean.getTableName(),beans, cover);

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

    /**
     * 构建文件成功后执行方法
     * @param autoCodeConfig
     */
     void custom(AutoCodeConfig autoCodeConfig);


    /**
     * 开始
     */
    default void start(AutoCodeConfig autoCodeConfig){
        if(autoCodeConfig==null){
            autoCodeConfig = saxYaml();
        }
        List<TableConfig> tableConfigs = saxTable(autoCodeConfig);
        AutoCodeConfig finalAutoCodeConfig = autoCodeConfig;
        tableConfigs.forEach(t->{
            logger.info("----------------------------------------单表开始生成{}",t.getDataName());
            Bean bean=new Bean();
            bean.setTableName(t.getAliasName());
            bean.setDataName(t.getDataName());
            finalAutoCodeConfig.setBean(bean);
            build(finalAutoCodeConfig);
            logger.info("----------------------------------------单表生成结束{}",t.getDataName());
        });
    }
}