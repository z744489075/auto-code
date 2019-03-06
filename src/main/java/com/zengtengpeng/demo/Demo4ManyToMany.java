package com.zengtengpeng.demo;


import com.zengtengpeng.autoCode.StartCode;
import com.zengtengpeng.relation.utils.RelationUtils;

/**
 * 多对多生成实例 test_user 多个用户 对应 test_role 多个角色
 */
public class Demo4ManyToMany {
    public static void main(String[] args) {
        //普通写法
        /*RelationUtils.manyToMany(StartCode.saxYaml(), new StartCode() {
            @Override
            public void custom(AutoCodeConfig autoCodeConfig) {

            }
        }, new BuildManyToMany() {
            @Override
            public void custom(AutoCodeConfig autoCodeConfig) {

            }
        });*/
        //lambda表达式写法 二选一
        RelationUtils.manyToMany(StartCode.saxYaml("auto-code_many-to-many.yaml"), t->{}, rt -> {});
    }
}
