
package com.zengtengpeng.demo;


import com.zengtengpeng.autoCode.StartCode;
import com.zengtengpeng.relation.utils.RelationUtils;

/**
 * 一对多生成实例 test_user 一个用户 对应 test_addr 多个收货地址
 */
public class Demo3OneToMany {
    public static void main(String[] args) {

        //普通写法 二选一
//        RelationUtils.oneToMany(StartCode.saxYaml(), new StartCode() {
//            @Override
//            public void custom(AutoCodeConfig autoCodeConfig) {
//
//            }
//        }, new BuildOneToMany() {
//            @Override
//            public void custom(AutoCodeConfig autoCodeConfig) {
//
//            }
//        });


        //lambda表达式写法 二选一
        RelationUtils.oneToMany(StartCode.saxYaml("auto-code_one-to-many.yaml"), t -> {}, rt -> {});
    }
}
