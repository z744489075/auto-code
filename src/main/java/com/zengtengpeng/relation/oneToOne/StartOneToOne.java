package com.zengtengpeng.relation.oneToOne;

import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.create.BuildController;
import com.zengtengpeng.relation.bean.RelationTable;

/**
 * 启动一对一构建
 */
public interface StartOneToOne {

    /**
     * 创建默认的controller
     * @return
     */
    default BuildOneToOneController buildOneToOneController(){
        return (p,k)->{};
    }
    /**
     * 创建默认的bean
     * @return
     */
    default BuildOneToOneBean buildOneToOneBean(){
        return (p,k)->{};
    }
    /**
     * 创建默认的dao
     * @return
     */
    default BuildOneToOneDao buildOneToOneDao(){
        return (p,k)->{};
    }
    /**
     * 创建默认的service
     * @return
     */
    default BuildOneToOneService buildOneToOneService(){
        return (p,k)->{};
    }
    /**
     * 创建默认的serviceImpl
     * @return
     */
    default BuildOneToOneServiceImpl buildOneToOneServiceImpl(){
        return (p,k)->{};
    }
    /**
     * 创建默认的xml
     * @return
     */
    default BuildOneToOneXml BuildOneToOneXml(){
        return (p,k)->{};
    }

    /**
     * 自定义
     * @return
     */
    void custom(RelationTable primaryKey, RelationTable foreign,AutoCodeConfig autoCodeConfig);
    /**
     * 构建
     * @param primaryKey
     * @param foreign
     */
    default void build(RelationTable primaryKey, RelationTable foreign,AutoCodeConfig autoCodeConfig){

        buildOneToOneController().build(primaryKey,foreign,autoCodeConfig);
        buildOneToOneBean().build(primaryKey,foreign,autoCodeConfig);
        buildOneToOneDao().build(primaryKey,foreign,autoCodeConfig);
        buildOneToOneService().build(primaryKey,foreign,autoCodeConfig);
        buildOneToOneServiceImpl().build(primaryKey,foreign,autoCodeConfig);
        BuildOneToOneXml().build(primaryKey,foreign,autoCodeConfig);
        custom(primaryKey,foreign,autoCodeConfig);
    }


}
