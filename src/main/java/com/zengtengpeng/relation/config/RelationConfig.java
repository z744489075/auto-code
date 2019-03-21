package com.zengtengpeng.relation.config;

import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.relation.bean.RelationTable;

/**
 * 关系描述表
 */
public class RelationConfig {
    /**
     * 主表
     */
    private RelationTable primary;

    /**
     * 外表
     */
    private RelationTable foreign;

    /**
     * 第三表
     */
    private RelationTable thirdparty;



    public RelationTable getPrimary() {
        return primary;
    }

    public void setPrimary(RelationTable primary) {
        this.primary = primary;
    }

    public RelationTable getForeign() {
        return foreign;
    }

    public void setForeign(RelationTable foreign) {
        this.foreign = foreign;
    }

    public RelationTable getThirdparty() {
        return thirdparty;
    }

    public void setThirdparty(RelationTable thirdparty) {
        this.thirdparty = thirdparty;
    }


}
