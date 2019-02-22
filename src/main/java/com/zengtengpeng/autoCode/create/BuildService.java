package com.zengtengpeng.autoCode.create;

import com.zengtengpeng.autoCode.bean.BuildJavaBean;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.generator.utils.MyStringUtils;
import com.zengtengpeng.jdbc.bean.Bean;

import java.util.List;

/**
 * 生成servce
 */
@FunctionalInterface
public interface BuildService {

    StringBuffer stringBuffer = new StringBuffer();

    default BuildService before(AutoCodeConfig autoCodeConfig){
        Bean bean = autoCodeConfig.getBean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        BuildJavaConfig buildJavaConfig = autoCodeConfig.getBuildJavaConfig();
        MyStringUtils.append(stringBuffer,"package %s.%s;",globalConfig.getParentPack(),globalConfig.getPackageService());
        buildJavaConfig.getImports().forEach(t->stringBuffer.append("import "+t+";\n"));
        /*public interface TestCodeDao extends BaseDao<TestCode> {
        }*/
        stringBuffer.append("\n\n");
        StringBuffer s=new StringBuffer();
        buildJavaConfig.getExtend().forEach(t-> s.append(t+","));
        List<String> annotations = buildJavaConfig.getAnnotations();

        if(annotations!=null){
            annotations.forEach(t->stringBuffer.append(t+"\n"));
        }
        //public interface SysLoginLogService extends  BaseService<SysLoginLog, SysLoginLogDao>
        MyStringUtils.append(stringBuffer,"public interface %s%s extends %s {",
                bean.getTableName(),MyStringUtils.firstUpperCase(globalConfig.getPackageService()),s.substring(0,s.length()-1));
        return this;
    }


    /**
     * 结束
     * @param autoCodeConfig
     * @return
     */
    default BuildService end(AutoCodeConfig autoCodeConfig){
        stringBuffer.append("}\n");
        return this;
    }

    /**
     * 自定义sql
     * @param
     * @return
     */
    List<BuildJavaBean> custom(AutoCodeConfig autoCodeConfig);

    /**
     * 构建dao
     * @param autoCodeConfig
     * @return
     */
    default String buildService(AutoCodeConfig autoCodeConfig){
        BuildService before = before(autoCodeConfig);
        List<BuildJavaBean> custom = before.custom(autoCodeConfig);
        if(custom!=null){
            custom.forEach(t->{
                List<String> annotation = t.getAnnotation();
                if(annotation!=null){
                    annotation.forEach(ttt-> MyStringUtils.append(stringBuffer,"%s",1,ttt));
                }
                StringBuffer params=new StringBuffer();
                List<String> params1 = t.getParams();
                if(params1!=null){
                    params1.forEach(tt-> params.append(tt+","));
                }

                stringBuffer.append(String.format("\t %s %s(%s)",t.getReturnType(),t.getMethodName(),params.substring(0,params.length()-1)));

                if(MyStringUtils.isEmpty(t.getContent())){
                    stringBuffer.append(";\n");
                }else {
                    stringBuffer.append("{");
                    MyStringUtils.append(stringBuffer,"%s",2,t.getContent());
                    MyStringUtils.append(stringBuffer,"}",1);

                }});
        }

        before.end(autoCodeConfig);
        return stringBuffer.toString();
    }


}
