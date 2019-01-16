package com.zengtengpeng.generator.create.impl;

import com.zengtengpeng.generator.bean.AutoCodeParam;
import com.zengtengpeng.generator.create.CreateCode;

/**
 * 生成基础的增删改查
 */
public class BaseCreateCode implements CreateCode {

    /**
     * 初始化
     * @param basePackagePath 模板路径
     */
    public BaseCreateCode(String basePackagePath) {
        cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(),basePackagePath);
    }

    @Override
    public void startAuto( AutoCodeParam param) {
        createJavaFile("controller",  param);
        createJavaFile("service",  param);
        createJavaFile("serviceImpl", param);
    }
}
