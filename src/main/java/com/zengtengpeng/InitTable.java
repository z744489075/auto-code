package com.zengtengpeng;

import com.zengtengpeng.autoCode.StartCode;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.utils.MyStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 初始化表数据
 */
public class InitTable {

   static Logger logger = LoggerFactory.getLogger(InitTable.class);
    public static void main(String[] args) {
        StartCode startCode= t->{};
        startCode.start(StartCode.saxYaml());
    }
}
