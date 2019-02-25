package com.zengtengpeng.autoCode;

import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.create.BuildDao;
import com.zengtengpeng.autoCode.create.BuildService;
import com.zengtengpeng.autoCode.create.BuildXml;
import com.zengtengpeng.generator.utils.MyStringUtils;
import com.zengtengpeng.jdbc.bean.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 开始构建
 */
public interface StartBuild {

    /**
     * 构建xml
     * @param autoCodeConfig
     * @return
     */
     default String buildXml(AutoCodeConfig autoCodeConfig){
        BuildXml buildXml=autoCodeConfig.getBuildXml();
        return buildXml.buildSql(autoCodeConfig);
    }



}
