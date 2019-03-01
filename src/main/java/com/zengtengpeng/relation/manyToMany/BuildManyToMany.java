package com.zengtengpeng.relation.manyToMany;

import com.zengtengpeng.autoCode.config.AutoCodeConfig;
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
    void custom(AutoCodeConfig autoCodeConfig);
    /**
     * 构建
     * @param autoCodeConfig
     */
    default void build(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        logger.info("-------------------------------------开始构建多对多 ->主表:{} ->外表{}", primary.getBeanName(),foreign.getBeanName());
        BuildManyToManyController().build(autoCodeConfig);
        BuildManyToManyBean().build(autoCodeConfig);
        BuildManyToManyDao().build(autoCodeConfig);
        BuildManyToManyService().build(autoCodeConfig);
        BuildManyToManyServiceImpl().build(autoCodeConfig);
        BuildManyToManyXml().build(autoCodeConfig);
        custom(autoCodeConfig);
        logger.info("--------------------------------------构建多对多结束 ->主表:{} ->外表{}",primary.getBeanName(),foreign.getBeanName());
    }


}
