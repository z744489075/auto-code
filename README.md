# auto-code
欢迎使用auto-code代码自动生成引擎, 2.0重大升级.支持`单表`, `一对一`, `一对多` ,`多对多`代码生成  [源码地址](https://gitee.com/ztp/auto-code)

# 目录
1. <a href="#1">项目介绍</a>
    1. <a href="#1.1">项目的优势在哪里</a>
    2. <a href="#1.2">什么情况选择该项目</a>
    3. <a href="#1.3">为何会发起该项目</a>
    4. <a href="#1.4">如果您觉得项目还行.请点赞.您的支持是我最大的动力项目地址</a>
2. <a href="#2">集成教程</a>
    1. <a href="#2.1">使用教程</a>
        1. <a href="#2.1.1">准备工作</a>
        2. <a href="#2.1.2">单表生成</a>
        3. <a href="#2.1.3">一对一代码生成 one-to-one</a>
        4. <a href="#2.1.4">一对多 代码生成 one-to-Many</a>
        5.  <a href="#2.1.5">多对多 代码生成 many-to-many</a>
    2.  <a href="#2.2">生成代码注意事项</a>

## 更新日志

> 2019-03-21: 版本:2.1.0 生成代码增加可视化视图,不再需要写yaml文件配置了

> 2019-03-18: 版本:2.0.2 增加swagger api配置

    1.yaml 文件 globalConfig 增加 swagger:true 
    2.集成swagger(具体请看实例代码.或者百度),访问 http://localhost:8080/swagger-ui.html#/


## <a name="1">项目介绍</a>
### <a name="1.1">项目的优势在哪里</a>

> 1.目前市面上的代码生成工具绝大多数仅仅支持生成单表,该项目支持 `单表`, `一对一`, `一对多` ,`多对多` 
代码生成.大大简化了开发的工作量

> 2.只要目前你的项目采用 springMVC+spring+mybatis架构的项目都适用(传统工程和springBoot工程都适用).
不管一次开发还是二次开发.该项目仅仅只是帮你生成单表以及多表的`增删改查`,不做任何底层的改动.

### <a name="1.2">什么情况选择该项目</a>
> 1.该项目只生成接口(controller,service,serviceImpl,dao,xml),
不生成页面.所以如果项目是采用前后台分离,不需要写页面.该项目会适合你

> 2.如果还想生成页面请看该项目,这个项目基于本项目.扩展了页面生成.适合后台使用 [源码地址](https://gitee.com/ztp/auto-code-admin) 
 [演示地址](http://www.zengtengpeng.com/login/gotoLogin) 账号 `ztp`  密码 `111111`
 
### <a name="1.3">为何会发起该项目? </a>

> 绝大多数时候我们都是在做`增删改查`.每次创建一张表.然后我们需要重新写一次增删改查,
写虽然简单,不过极度耗时(controller,server,serverImpl,dao,xml) 
    所以才有了该项目,该项目能帮助你减少70%的工作量,让你专注于业务的实现.

### <a name="1.4">如果您觉得项目还行.请点赞.您的支持是我最大的动力[项目地址](https://gitee.com/ztp/auto-code)</a>

![start](http://images.zengtengpeng.com/auto-code/start.png)

## <a name="2">集成教程</a>

> 1.传统java-web集成请看这里 [项目地址](https://gitee.com/ztp/auto-code-ui)

> 2. spring-boot集成请看这里 [项目地址](https://gitee.com/ztp/auto-code-spring-boot-starter)


### <a name="2.1">使用教程</a>
#### <a name="2.1.1">准备工作</a>

>1.集成非常简单请查看上面的项目.选择自己需要的方式.里面有实例工程可供参考

>2.成功后访问 http://localhost:8070/auto-code-ui/ui/index.html

界面如下: 
![global](http://images.zengtengpeng.com/auto-code-ui/global.png)

#### <a name="2.1.2">单表生成</a>

>假设我们要生成一张单表
```sql
CREATE TABLE `test_simple_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `age` int(3) DEFAULT NULL COMMENT '年龄',
  `status` int(2) DEFAULT NULL COMMENT '{"name":"状态","1":"启用","0":"禁用"}',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `remarks` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='单表代码生成';
```

>1.在数据库建表

>2. 界面选择单表生成.选择对应的表,点击`生成预览`. 最后点击`确认生成`. 完毕.

![simple](http://images.zengtengpeng.com/auto-code-ui/simple.png)

>3.生成完毕 
#### <a name="2.1.3">一对一代码生成 one-to-one (代码采用追加的方式.无需担心代码被覆盖) </a>

>`一个用户`对应`一个班级`
```sql
CREATE TABLE `test_one_to_one_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '名称',
  `age` INT(3) DEFAULT NULL COMMENT '年龄',
  `status` INT(2) DEFAULT NULL COMMENT '{"name":"状态","1":"启用","0":"禁用"}',
  `birthday` DATE DEFAULT NULL COMMENT '生日',
  `remarks` TEXT COMMENT '备注',
  `mun` DECIMAL(20,2) DEFAULT NULL COMMENT '数字',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 COMMENT='一对一用户';

CREATE TABLE `test_one_to_one_class` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '班级id',
  `user_id` INT(11) DEFAULT NULL COMMENT '用户id',
  `class_name` VARCHAR(50) DEFAULT NULL COMMENT '班级名称',
  `quantity` INT(11) DEFAULT NULL COMMENT '班级人数',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='一对一班级';

```

>1. 打开界面.选择对应的表,一对一比单表多了一个主外表的关联关系id.
比如上面两张表的关系就是 test_one_to_one_user.id=test_one_to_one_class.user_id,在页面吧主外键选择好就行

![one-to-one](http://images.zengtengpeng.com/auto-code-ui/one-to-one1.png)
![one-to-one](http://images.zengtengpeng.com/auto-code-ui/one-to-one2.png)

#### <a name="2.1.4">一对多 代码生成 one-to-Many (代码采用追加的方式.无需担心代码被覆盖)</a>

>  `一对多`与`一对一`一样不再描述


#### <a name="2.1.5">多对多 代码生成 many-to-many (代码采用追加的方式.无需担心代码被覆盖)</a>
    
> `多个用户` 对应 `多个角色`

```sql
CREATE TABLE `test_many_to_many_role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '角色',
  `name` VARCHAR(100) NOT NULL COMMENT '角色名称',
  `status` INT(2) DEFAULT '0' COMMENT '{"name":"状态","0":"启用","1":"禁用"}',
  `create_user_id` INT(11) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` INT(11) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `dels` INT(2) DEFAULT '0' COMMENT '{"name":"是否删除","0":"正常","1":"删除"}',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='多对多角色';

CREATE TABLE `test_many_to_many_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '名称',
  `age` INT(3) DEFAULT NULL COMMENT '年龄',
  `status` INT(2) DEFAULT NULL COMMENT '{"name":"状态","1":"启用","0":"禁用"}',
  `birthday` DATE DEFAULT NULL COMMENT '生日',
  `remarks` TEXT COMMENT '备注',
  `mun` DECIMAL(20,2) DEFAULT NULL COMMENT '数字',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='多对多用户';

CREATE TABLE `test_many_to_many_user_role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '测试用户角色',
  `user_id` INT(11) DEFAULT NULL COMMENT '用户id',
  `role_id` INT(11) DEFAULT NULL COMMENT '角色id',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8 COMMENT='多对多用户角色';
```

> 1.打开界面.选择对应的表,`多对多`比`一对一`多了一个关系描述表
    比如上面三张张表的关系是通过 test_many_to_many_user_role来表述的 
    
    test_many_to_many_user_role.user_id=test_many_to_many_user.id and test_many_to_many_user_role.role_id=test_many_to_many_role.id

![many-to-many](http://images.zengtengpeng.com/auto-code-ui/many-to-many1.png)
![many-to-many](http://images.zengtengpeng.com/auto-code-ui/many-to-many2.png)
![many-to-many](http://images.zengtengpeng.com/auto-code-ui/many-to-many3.png)


### <a name="2.2">生成代码注意事项 </a>

    1.创建表结构时如果写上表与字段的注释将大大增加程序的可读性.我会将注释写到bean上面.
    2.如果注释为json键值对字符串我将会在实体类生成一个字典方法
    如:  {"1":"启用","0":"禁用"} 将会在实体类里面生成:
        public String getStatus_(){
    		if(MyStringUtils.isEmpty(status)){
    			 return "";
    		}else if(status.equals("1")){
    			return "启用";
    		}else if(status.equals("0")){
    			return "禁用";
    		}
    		return "";
    
    	}