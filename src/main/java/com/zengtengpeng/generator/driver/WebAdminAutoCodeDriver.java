package com.zengtengpeng.generator.driver;

import com.zengtengpeng.generator.create.impl.BaseCreateCode;
import com.zengtengpeng.generator.create.impl.WebAdminCreateCode;
import com.zengtengpeng.generator.utils.AutoCodeUtils;

/**
 * 驱动
 */
public class WebAdminAutoCodeDriver {
    static {
        AutoCodeUtils.createCode=new WebAdminCreateCode("/ftl/web-admin");
    }
}
