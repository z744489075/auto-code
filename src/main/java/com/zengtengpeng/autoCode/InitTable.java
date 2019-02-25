package com.zengtengpeng.autoCode;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.autoCode.config.BuildJavaConfig;
import com.zengtengpeng.autoCode.config.GlobalConfig;
import com.zengtengpeng.autoCode.config.TableConfig;
import com.zengtengpeng.autoCode.create.BuildDao;
import com.zengtengpeng.jdbc.bean.Bean;
import com.zengtengpeng.jdbc.utils.JDBCUtils;
import org.yaml.snakeyaml.Yaml;

import java.sql.Connection;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 初始化表数据
 */
public class InitTable {

    public static void main(String[] args) {
        Yaml yaml=new Yaml();
        System.out.println(JDBCType.valueOf(1).getName());
        AutoCodeConfig autoCodeConfig = yaml.loadAs(InitTable.class.getClassLoader().getResourceAsStream("auto-code.yaml"), AutoCodeConfig.class);
        System.out.println(autoCodeConfig);
        Connection connection = JDBCUtils.getConnection(autoCodeConfig.getDatasourceConfig());
        Bean bean=new Bean();
        GlobalConfig globalConfig = autoCodeConfig.getGlobalConfig();
        List<TableConfig> tableNames = globalConfig.getTableNames();

        tableNames.forEach(t->{
            bean.setTableName(t.getAliasName());
            bean.setDataName(t.getDataName());
            if(JDBCUtils.getTable(connection,bean)!=null){
                //构建xml
                autoCodeConfig.setBean(bean);
                //xml
                System.out.println(autoCodeConfig.getBuildXml().buildSql(autoCodeConfig));
                //构建dao
                System.out.println(autoCodeConfig.getBuildDao().buildDao(autoCodeConfig));

                //国际server
                autoCodeConfig.setBuildService(tt->{
                    BuildJavaConfig buildJavaBean=new BuildJavaConfig();
                    List<BuildJavaField> bf=new ArrayList<>();
                    BuildJavaField buildJavaField=new BuildJavaField();
                    buildJavaField.setAnnotation(Arrays.asList("@Deser","@desc"));
                    buildJavaField.setFiledName("test");
                    buildJavaField.setReturnType("String");
                    buildJavaField.setInit("null");
                    bf.add(buildJavaField);
                    BuildJavaField buildJavaField1=new BuildJavaField();
                    buildJavaField1.setAnnotation(Arrays.asList("@Deser","@desc"));
                    buildJavaField1.setFiledName("test");
                    buildJavaField1.setReturnType("String");
                    buildJavaField1.setInit("null");
                    bf.add(buildJavaField1);
                    buildJavaBean.setBuildJavaFields(bf);
                    List<String> list=new ArrayList<>();
                    list.add("dddddddd.ddddddd");
                    list.add("zzzzzzz.zzzzzzzzz");
                    buildJavaBean.setImports(list);

                    List<BuildJavaMethod> des=new ArrayList<>();
                    BuildJavaMethod dess=new BuildJavaMethod();
                    dess.setContent("fwfafeafewaf");
                    dess.setMethodName("test");
                    dess.setMethodType("public");
                    dess.setReturnType("String");
                    des.add(dess);
                    BuildJavaMethod dess1=new BuildJavaMethod();
                    dess1.setContent("fwfafeafewaf");
                    dess1.setMethodName("test");
                    dess1.setMethodType("public");
                    dess1.setReturnType("String");
                    List<String> an=new ArrayList<>();
                    an.add("@A");
                    an.add("@B");
                    an.add("@C");
                    dess1.setAnnotation(an);
                    List<String> pa=new ArrayList<>();
                    pa.add("String a");
                    dess1.setParams(pa);
                    des.add(dess1);
                    buildJavaBean.setBuildJavaMethods(des);
                    return buildJavaBean;
                });
                System.out.println(autoCodeConfig.getBuildService().buildService(autoCodeConfig));

            }
        });


    }
}
