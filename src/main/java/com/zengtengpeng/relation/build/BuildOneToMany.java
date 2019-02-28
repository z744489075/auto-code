package com.zengtengpeng.relation.build;

import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.config.RelationConfig;
import com.zengtengpeng.relation.oneToMany.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动一对多构建
 */
public interface BuildOneToMany {
    Logger logger = LoggerFactory.getLogger(BuildOneToMany.class);
    /**
     * 创建默认的controller
     * @return
     */
    default BuildOneToManyController BuildOneToManyController(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的bean
     * @return
     */
    default BuildOneToManyBean BuildOneToManyBean(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的dao
     * @return
     */
    default BuildOneToManyDao BuildOneToManyDao(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的service
     * @return
     */
    default BuildOneToManyService BuildOneToManyService(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的serviceImpl
     * @return
     */
    default BuildOneToManyServiceImpl BuildOneToManyServiceImpl(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的xml
     * @return
     */
    default BuildOneToManyXml BuildOneToManyXml(){
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
        logger.info("-------------------------------------开始构建一对多 ->主键:{} ->外表{}", primary.getBeanName(),foreign.getBeanName());
        BuildOneToManyController().build(relationConfig);
        BuildOneToManyBean().build(relationConfig);
        BuildOneToManyDao().build(relationConfig);
        BuildOneToManyService().build(relationConfig);
        BuildOneToManyServiceImpl().build(relationConfig);
        BuildOneToManyXml().build(relationConfig);
        custom(relationConfig);
        logger.info("--------------------------------------构建一对多结束 ->主键:{} ->外表{}",primary.getBeanName(),foreign.getBeanName());
    }


}
