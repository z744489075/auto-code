package com.zengtengpeng.demo;

import com.zengtengpeng.autoCode.StartCode;

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

        startCode.start(StartCode.saxYaml("auto-code_simple.yaml"));
    }
}
