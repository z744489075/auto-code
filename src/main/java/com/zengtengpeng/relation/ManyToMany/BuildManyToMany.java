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
    default BuildManyToManyController BuildManyToManyController(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的bean
     * @return
     */
    default BuildManyToManyBean BuildManyToManyBean(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的dao
     * @return
     */
    default BuildManyToManyDao BuildManyToManyDao(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的service
     * @return
     */
    default BuildManyToManyService BuildManyToManyService(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的serviceImpl
     * @return
     */
    default BuildManyToManyServiceImpl BuildManyToManyServiceImpl(){
        return (rt, p, f)->{};
    }
    /**
     * 创建默认的xml
     * @return
     */
    default BuildManyToManyXml BuildManyToManyXml(){
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
        BuildManyToManyController().build(relationConfig);
        BuildManyToManyBean().build(relationConfig);
        BuildManyToManyDao().build(relationConfig);
        BuildManyToManyService().build(relationConfig);
        BuildManyToManyServiceImpl().build(relationConfig);
        BuildManyToManyXml().build(relationConfig);
        custom(relationConfig);
        logger.info("--------------------------------------构建一对多结束 ->主键:{} ->外表{}",primary.getBeanName(),foreign.getBeanName());
    }


}
