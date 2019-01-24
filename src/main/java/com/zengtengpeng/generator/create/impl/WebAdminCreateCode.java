package com.zengtengpeng.generator.create.impl;

import com.zengtengpeng.generator.bean.AutoCodeParam;
import com.zengtengpeng.generator.create.CreateCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 生成增删改查加页面
 */
public class WebAdminCreateCode implements CreateCode {

    Logger logger = LoggerFactory.getLogger(WebAdminCreateCode.class);
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
        int index=0;
        //根权限
        String sql1 = String.format("INSERT INTO `sys_auth` (`parent_auth_id`,`name`,`sort`,`href`,`icon`,`shows`,`create_time`) VALUES" +
                        "  ('%s','%s','%s','%s','%s','%s',NOW());", 3, param.getTableRemarks(), 1, param.getTableValue() + "/gotoList",
                "layui-icon layui-icon-file-b", "0");
        //查询
        String sql2 = String.format("INSERT INTO `sys_auth` (`parent_auth_id`,`name`,`sort`,`href`,`icon`,`shows`,`create_time`) VALUES" +
                        "  ((SELECT id FROM `sys_auth` a WHERE a.name='%s'),'%s','%s','%s','%s','%s',NOW());",param.getTableRemarks(),"查询",++index,param.getTableValue()+"/selectAll",
                "layui-icon layui-icon-search","1");
        //导出
        String sql3 = String.format("\nINSERT INTO `sys_auth` (`parent_auth_id`,`name`,`sort`,`href`,`icon`,`shows`,`create_time`) VALUES" +
                        "  ((SELECT id FROM `sys_auth` a WHERE a.name='%s'),'%s','%s','%s','%s','%s',NOW());",param.getTableRemarks(),"导出",++index,param.getTableValue()+"/export",
                "layui-icon layui-icon-next","1");
        //编辑
        String sql4 = String.format("\nINSERT INTO `sys_auth` (`parent_auth_id`,`name`,`sort`,`href`,`icon`,`shows`,`create_time`) VALUES" +
                        "  ((SELECT id FROM `sys_auth` a WHERE a.name='%s'),'%s','%s','%s','%s','%s',NOW());",param.getTableRemarks(),"编辑",++index,param.getTableValue()+"/save",
                "layui-icon layui-icon-edit","1");
        //删除
        String sql5 = String.format("\nINSERT INTO `sys_auth` (`parent_auth_id`,`name`,`sort`,`href`,`icon`,`shows`,`create_time`) VALUES" +
                        "  ((SELECT id FROM `sys_auth` a WHERE a.name='%s'),'%s','%s','%s','%s','%s',NOW());",param.getTableRemarks(),"删除",++index,param.getTableValue()+"/deleteByPrimaryKey",
                "layui-icon layui-icon-fonts-del","1");

        logger.info("权限SQL:请注意表名注释一定要唯一,权限会找不到父ID\n{}\n{}\n{}\n{}\n{}",sql1,sql2,sql3,sql4,sql5);

    }
}
