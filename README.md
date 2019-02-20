# auto-code
    欢迎使用auto-code代码生成引擎
### 介绍
此分支正在重写底层实现
auto-code 可以生成controller,server,dao,xml以及view代码

#### 软件架构

基于mybatis-generator定制的代码生成器.使用 pagehelper进行分页.已jar包的形式引入进去


#### 安装教程 (已经上传maven公共仓库所以不再需要私服)

2.引入jar
```
        <dependency>
            <groupId>com.zengtengpeng</groupId>
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
    		startCode.setParentPack("com.zengtengpeng.newYear");
    		AutoCodeUtils.startByBaseCode(startCode);
    	}
    }
```

#### demo地址 [传送门](https://gitee.com/ztp/auto-code-demo)

