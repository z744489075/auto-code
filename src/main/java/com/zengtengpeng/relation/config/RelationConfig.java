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


    /**
     * 如果检测到存在是否继续增加 true 继续增加  false不增加
     */
    private Boolean exist=false;

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

    public Boolean getExist() {
        return exist;
    }

    public void setExist(Boolean exist) {
        this.exist = exist;
    }

}
