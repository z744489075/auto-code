package com.zengtengpeng.autoCode.config;

import com.zengtengpeng.autoCode.utils.MyStringUtils;

public class TableConfig {
    //表名称
    private String dataName;

    //别名 不写默认采用驼峰命名法 test_code->TestCode
    private String aliasName;


    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getAliasName() {
        if(MyStringUtils.isEmpty(aliasName)){
            return MyStringUtils.upperCase_(dataName,true);
        }
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
