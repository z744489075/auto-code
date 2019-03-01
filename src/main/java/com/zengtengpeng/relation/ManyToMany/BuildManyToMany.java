package com.zengtengpeng.relation.ManyToMany;

import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.config.RelationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动多对多构建
 */
public interface BuildManyToMany {
    Logger logger = LoggerFactory.getLogger(BuildManyToMany.class);
    /**
     * 创建默认的controller
     * @return
     */
    default BuildManyToManyController BuildOneToManyController(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的bean
     * @return
     */
    default BuildManyToManyBean BuildOneToManyBean(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的dao
     * @return
     */
    default BuildManyToManyDao BuildOneToManyDao(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的service
     * @return
     */
    default BuildManyToManyService BuildOneToManyService(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的serviceImpl
     * @return
     */
    default BuildManyToManyServiceImpl BuildOneToManyServiceImpl(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的xml
     * @return
     */
    default BuildManyToManyXml BuildOneToManyXml(){
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
