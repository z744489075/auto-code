package com.zengtengpeng.demo;

import com.zengtengpeng.autoCode.StartCode;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.jdbc.utils.JDBCUtils;

import java.sql.Connection;
import java.util.List;

/**
 * 单表生成实例
 */
public class Demo1simple {
    public static void main(String[] args) {
        //lambda表达式写法 二选一
        StartCode startCode= t->{};

        //普通写法 二选一
//        StartCode startCode=new StartCode() {
//            @Override
//            public void custom(AutoCodeConfig autoCodeConfig) {
//
//            }
//        };
        AutoCodeConfig autoCodeConfig = StartCode.saxYaml("auto-code_simple.yaml");
        Connection connection = startCode.getConnection(autoCodeConfig);
        List<String> tablesName = JDBCUtils.getTablesName(connection,null);
        System.out.println(tablesName);
//        startCode.start(autoCodeConfig);
    }
}
