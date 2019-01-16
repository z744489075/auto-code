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
    
    public class AutoCodeTest {
    	public static void main(String[] args) throws Exception {
    
    		List<String> dataNames= Arrays.asList("test_auto_code2");
    		StartCode startCode=new StartCode();
    		startCode.setJdbc("jdbc:mysql://localhost:3306/test_etl_bank");
    		startCode.setUser("root");
    		startCode.setPassword("111111");
    		startCode.setDataNames(dataNames);
    		startCode.setParentPath("f:/core");
    		startCode.setParentPack("com.etiaolong.newYear");
    		AutoCodeUtils.startByBaseCode(startCode);
    	}
    
    }
```

#### demo地址 [传送门](https://gitee.com/ztp/auto-code-demo)

