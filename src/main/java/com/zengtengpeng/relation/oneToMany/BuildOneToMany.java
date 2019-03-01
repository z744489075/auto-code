package com.zengtengpeng.relation.oneToMany;

import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.config.RelationConfig;
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
    void custom(AutoCodeConfig autoCodeConfig);
    /**
     * 构建
     * @param autoCodeConfig
     */
    default void build(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable primary = relationConfig.getPrimary();
        RelationTable foreign = relationConfig.getForeign();
        logger.info("-------------------------------------开始构建一对多 ->主表:{} ->外表{}", primary.getBeanName(),foreign.getBeanName());
        BuildOneToManyController().build(autoCodeConfig);
        BuildOneToManyBean().build(autoCodeConfig);
        BuildOneToManyDao().build(autoCodeConfig);
        BuildOneToManyService().build(autoCodeConfig);
        BuildOneToManyServiceImpl().build(autoCodeConfig);
        BuildOneToManyXml().build(autoCodeConfig);
        custom(autoCodeConfig);
        logger.info("--------------------------------------构建一对多结束 ->主表:{} ->外表{}",primary.getBeanName(),foreign.getBeanName());
    }


}
