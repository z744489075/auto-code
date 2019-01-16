package com.zengtengpeng.generator.driver;

import com.zengtengpeng.generator.create.impl.BaseCreateCode;
import com.zengtengpeng.generator.utils.AutoCodeUtils;

/**
 * 驱动
 */
public class BaseAutoCodeDriver {
    static {
        AutoCodeUtils.createCode=new BaseCreateCode("/ftl/base");
    }
}
