package com.zengtengpeng.generator.create.impl;

import com.zengtengpeng.generator.bean.AutoCodeParam;
import com.zengtengpeng.generator.create.CreateCode;


/**
 * 生成增删改查加页面
 */
public class WebAdminCreateCode implements CreateCode {

    /**
     * 初始化
     * @param basePackagePath 模板路径
     */
    public WebAdminCreateCode(String basePackagePath) {
        cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(),basePackagePath);
    }

    @Override
    public void startAuto(AutoCodeParam param) {
        createJavaFile("controller",  param);
        createJavaFile("service",  param);
        createJavaFile("serviceImpl",  param);
        createPageFile("list_page",param);
        createPageFile("detail_page",param);
    }
}
