package com.zengtengpeng.relation.oneToOne;

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
     void custom(RelationConfig relationConfig);
    /**
     * 构建
     */
    default void build(RelationConfig relationConfig){
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        logger.info("-------------------------------------开始构建一对一 ->主键:{} ->外表{}", primary.getBeanName(),foreign.getBeanName());
        buildOneToOneController().build(relationConfig);
        buildOneToOneBean().build(relationConfig);
        buildOneToOneDao().build(relationConfig);
        buildOneToOneService().build(relationConfig);
        buildOneToOneServiceImpl().build(relationConfig);
        BuildOneToOneXml().build(relationConfig);
        custom(relationConfig);
        logger.info("--------------------------------------构建一对一结束 ->主键:{} ->外表{}",primary.getBeanName(),foreign.getBeanName());
    }


}
