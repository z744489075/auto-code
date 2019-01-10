# auto-code

#### 介绍
springboot代码自动生成器

#### 软件架构

基于mybatis-generator定制的代码生成器.使用 pagehelper进行分页


#### 安装教程

##### 一.引用私服jar
1.pom.xml增加私服仓库(setting.xml也行)
```
<repositories>
		<repository>
			<id>nexus</id>
			<name>nexus Repository</name>
			<url>http://nexus.zengtengpeng.com/repository/maven-public/</url>
			<releases>
				<enabled>true</enabled>
			</releases>
			<!--snapshots默认是关闭的,需要开启  -->
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
		</repository>
	</repositories>
	
```

2.引入jar
```
        <dependency>
			<groupId>com.zengtengpeng.auto-code</groupId>
			<artifactId>auto-code</artifactId>
			<version>1.0.1</version>
		</dependency>
```


    

##### 二. 自行编译
1. 下载源码
2. 将项目打成jar上传到maven私服(请一定要将pom也上传到私服不然会引起找不到引用问题,最好使用idea集成的插件就不需要担心该问题)
    
```
    <build>
		<plugins>
			<!-- 要将源码放上去，需要加入这个插件 -->
			<plugin>
				<artifactId>maven-source-plugin</artifactId>
				<configuration>
					<attach>true</attach>
				</configuration>
				<executions>
					<execution>
						<phase>compile</phase>
						<goals>
							<goal>jar</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
	<distributionManagement>
		<repository>
		    <!-- 上传的id请自行在setting文件中配置-->
			<id>nexus</id>
			<url>http://123.59.134.198:21107/repository/etiaolong/</url>
		</repository>

		<snapshotRepository>
		    <!-- 上传的id请自行在setting文件中配置-->
			<id>nexus</id>
			<url>http://123.59.134.198:21107/repository/etiaolong/</url>
		</snapshotRepository>
	</distributionManagement>
```
3. 在需要的项目中引入对应的jar
    
```
        <dependency>
			<groupId>com.zengtengpeng.auto-code</groupId>
			<artifactId>auto-code</artifactId>
			<version>1.0.1</version>
		</dependency>
```


#### 使用说明


```
    调用
    
    /**
	 * 生成代码
	 * @param parentPath 生产代码的父路径
	 * @param parentPack 生产代码的父包
	 * @param jdbc jdbc连接
	 * @param user 数据库账号
	 * @param password 数据库密码
	 * @param dataNames 需要创建的数据库表明
	 * @throws IOException
	 * @throws TemplateException
	 */
    
    List<String> dataNames= Arrays.asList("test_auto_code");
		AutoCodeMain.create("E:\\resource\\workspaceKotlin\\testAutoCode","com.testAutoCode.newYear","jdbc:mysql://localhost:3336/test_etl_bank",
				"test_etl_bank","Ty9lx_nKB4T2DO",dataNames);
```

#### demo地址 [传送门](https://gitee.com/ztp/auto-code-demo)

