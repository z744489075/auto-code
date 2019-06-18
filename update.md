
>2019-05-22:版本2.1.5

1. 批量生成现在支持定义表别名了.

>2019-05-22:版本2.1.4

1.修复 https://gitee.com/ztp/auto-code/issues/IWSBT oracle 默认值问题
2.修复 https://gitee.com/ztp/auto-code/issues/IWPNM mysql bit获取不了长度问题 

> 2019-05-15:版本2.1.3

1.兼容oracle

>2019-05-09: 版本 2.1.2

1.移除Swagger(这个东西代码入侵性太强.里面也有一些BUG),
如需使用请在自己的项目增加swagger jar包  然后将 globalConfig.sswagger 设置为true


> 2019-05-08: 版本 2.1.1

1.生成的bean不再忽略转译之前的get方法了

2.`BaseDao`,`BaseService` 增加 `selectByConditionFirst`方法.返回第一条记录

> 2019-03-21: 版本:2.1.0 生成代码增加可视化视图,不再需要写yaml文件配置了

> 2019-03-18: 版本:2.0.2 增加swagger api配置

    1.yaml 文件 globalConfig 增加 swagger:true 
    2.集成swagger(具体请看实例代码.或者百度),访问 http://localhost:8080/swagger-ui.html#/
