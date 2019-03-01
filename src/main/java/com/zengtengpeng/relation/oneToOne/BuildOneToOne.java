package com.zengtengpeng.relation.oneToOne;

import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动一对一构建
 */
public interface BuildOneToOne {
    Logger logger = LoggerFactory.getLogger(BuildOneToOne.class);
    /**
     * 创建默认的controller
     * @return
     */
    default BuildOneToOneController buildOneToOneController(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的bean
     * @return
     */
    default BuildOneToOneBean buildOneToOneBean(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的dao
     * @return
     */
    default BuildOneToOneDao buildOneToOneDao(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的service
     * @return
     */
    default BuildOneToOneService buildOneToOneService(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的serviceImpl
     * @return
     */
    default BuildOneToOneServiceImpl buildOneToOneServiceImpl(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的xml
     * @return
     */
    default BuildOneToOneXml BuildOneToOneXml(){
        return (rt, p, f)->{};
    }

    /**
     * 自定义
     * @return
     */
     void custom(AutoCodeConfig autoCodeConfig);
    /**
     * 构建
     * @param autoCodeConfig
     */
    default void build(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        logger.info("-------------------------------------开始构建一对一 ->主表:{} ->外表{}", primary.getBeanName(),foreign.getBeanName());
        buildOneToOneController().build(autoCodeConfig);
        buildOneToOneBean().build(autoCodeConfig);
        buildOneToOneDao().build(autoCodeConfig);
        buildOneToOneService().build(autoCodeConfig);
        buildOneToOneServiceImpl().build(autoCodeConfig);
        BuildOneToOneXml().build(autoCodeConfig);
        custom(autoCodeConfig);
        logger.info("--------------------------------------构建一对一结束 ->主表:{} ->外表{}",primary.getBeanName(),foreign.getBeanName());
    }


}
