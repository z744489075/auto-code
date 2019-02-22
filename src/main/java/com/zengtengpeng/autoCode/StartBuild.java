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

    /**
     * 构建dao
     * @param autoCodeConfig
     * @return
     */
    default String buildDao(AutoCodeConfig autoCodeConfig){
        Bean bean = autoCodeConfig.getBean();
        BuildDao buildDao= autoCodeConfig.getBuildDao();
        BuildJavaConfig buildJavaConfig = autoCodeConfig.getBuildJavaConfig();
        BuildJavaConfig param=new BuildJavaConfig();
        List<String> imports=new ArrayList<>();
        imports.add("com.zengtengpeng.common.dao.BaseDao");
        imports.add(autoCodeConfig.getGlobalConfig().getParentPack()+".bean."+bean.getTableName());
        if(buildJavaConfig!=null && buildJavaConfig.getImplement()!=null){
            imports.addAll(buildJavaConfig.getImports());
        }
        param.setImports(imports);

        List<String> extend=new ArrayList<>();
        extend.add("BaseDao<"+bean.getTableName()+">");

        if(buildJavaConfig!=null && buildJavaConfig.getExtend()!=null){
            extend.addAll(buildJavaConfig.getExtend());
        }
        param.setExtend(extend);

        autoCodeConfig.setBuildJavaConfig(param);

        String s = buildDao.buildDao(autoCodeConfig);
        autoCodeConfig.setBuildJavaConfig(buildJavaConfig);
        return s;
    }
    /**
     * 构建Service
     * @param autoCodeConfig
     * @return
     */
    default String buildService(AutoCodeConfig autoCodeConfig){
        Bean bean = autoCodeConfig.getBean();
        BuildService buildService = autoCodeConfig.getBuildService();
        BuildJavaConfig buildJavaConfig = autoCodeConfig.getBuildJavaConfig();
        BuildJavaConfig param=new BuildJavaConfig();
        List<String> imports=new ArrayList<>();
        //import com.zengtengpeng.sys.bean.SysLoginLog;
        //import com.zengtengpeng.sys.dao.SysLoginLogDao;
        imports.add("com.zengtengpeng.common.dao.BaseService");
        String parentPack = autoCodeConfig.getGlobalConfig().getParentPack();
        imports.add(parentPack +".bean."+bean.getTableName());
        String daoName = bean.getTableName() + MyStringUtils.firstUpperCase(autoCodeConfig.getGlobalConfig().getPackageDao());
        imports.add(parentPack +".dao." + daoName);
        if(buildJavaConfig!=null && buildJavaConfig.getImplement()!=null){
            imports.addAll(buildJavaConfig.getImports());
        }
        param.setImports(imports);

        List<String> extend=new ArrayList<>();
        extend.add("BaseService<"+bean.getTableName()+","+daoName+">");

        if(buildJavaConfig!=null && buildJavaConfig.getExtend()!=null){
            extend.addAll(buildJavaConfig.getExtend());
        }
        param.setExtend(extend);

        autoCodeConfig.setBuildJavaConfig(param);
        String s = buildService.buildService(autoCodeConfig);
        autoCodeConfig.setBuildJavaConfig(buildJavaConfig);
        return s;
    }


}
