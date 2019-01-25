# auto-code
    欢迎使用auto-code代码生成引擎
### 介绍
auto-code 可以生成controller,server,dao,xml以及view代码

#### 软件架构

基于mybatis-generator定制的代码生成器.使用 pagehelper进行分页.已jar包的形式引入进去


#### 安装教程

##### 一.引用私服jar

> 1.maven setting.xml 私服配置  [下载地址](http://images.zengtengpeng.com/settings.xml)

```
    <!-- 增加账号密码 -->
    <servers>
        <server>
            <id>rdc-releases</id>
            <username>NGUWi1</username>
            <password>MTBW1BvT0v</password>
        </server>
        <server>
            <id>rdc-snapshots</id>
            <username>NGUWi1</username>
            <password>MTBW1BvT0v</password>
        </server>
    </servers>
    
    <!-- 配置私服地址 -->
    <profiles>
            <profile>
                <id>nexus</id>
                <repositories>
                    <repository>
                        <id>central</id>
                        <url>http://maven.aliyun.com/nexus/content/groups/public</url>
                        <releases>
                            <enabled>true</enabled>
                        </releases>
                        <snapshots>
                            <enabled>false</enabled>
                        </snapshots>
                    </repository>
                    <repository>
                        <id>snapshots</id>
                        <url>http://maven.aliyun.com/nexus/content/groups/public</url>
                        <releases>
                            <enabled>false</enabled>
                        </releases>
                        <snapshots>
                            <enabled>true</enabled>
                        </snapshots>
                    </repository>
                    <repository>
                        <id>rdc-releases</id>
                        <url>https://repo.rdc.aliyun.com/repository/50530-release-JLLiUK/</url>
                        <releases>
                            <enabled>true</enabled>
                        </releases>
                        <snapshots>
                            <enabled>false</enabled>
                        </snapshots>
                    </repository>
                    <repository>
                        <id>rdc-snapshots</id>
                        <url>https://repo.rdc.aliyun.com/repository/50530-snapshot-rV5sKk/</url>
                        <releases>
                            <enabled>false</enabled>
                        </releases>
                        <snapshots>
                            <enabled>true</enabled>
                        </snapshots>
                    </repository>
                </repositories>
                <pluginRepositories>
                    <pluginRepository>
                        <id>central</id>
                        <url>http://maven.aliyun.com/nexus/content/groups/public</url>
                        <releases>
                            <enabled>true</enabled>
                        </releases>
                        <snapshots>
                            <enabled>false</enabled>
                        </snapshots>
                    </pluginRepository>
                    <pluginRepository>
                        <id>snapshots</id>
                        <url>http://maven.aliyun.com/nexus/content/groups/public</url>
                        <releases>
                            <enabled>false</enabled>
                        </releases>
                        <snapshots>
                            <enabled>true</enabled>
                        </snapshots>
                    </pluginRepository>
                    <pluginRepository>
                        <id>rdc-releases</id>
                        <url>https://repo.rdc.aliyun.com/repository/50530-release-JLLiUK/</url>
                        <releases>
                            <enabled>true</enabled>
                        </releases>
                        <snapshots>
                            <enabled>false</enabled>
                        </snapshots>
                    </pluginRepository>
                    <pluginRepository>
                        <id>rdc-snapshots</id>
                        <url>https://repo.rdc.aliyun.com/repository/50530-snapshot-rV5sKk/</url>
                        <releases>
                            <enabled>false</enabled>
                        </releases>
                        <snapshots>
                            <enabled>true</enabled>
                        </snapshots>
                    </pluginRepository>
                </pluginRepositories>
            </profile>
        </profiles>
        
        <!-- 引用 -->
        <activeProfiles>
                <activeProfile>nexus</activeProfile>
        </activeProfiles>

```

2.引入jar
```
        <dependency>
			<groupId>com.zengtengpeng.auto-code</groupId>
			<artifactId>auto-code</artifactId>
			<version>1.0.0</version>
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

