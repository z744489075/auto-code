<?xml version="1.0"	encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration PUBLIC	"-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN" "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!-- classPathEntry:数据库的JDBC驱动,换成你自己的驱动位置.也可以将jar放到classpath下面 -->
    <!-- <classPathEntry location="D:\mysql-connector-java-5.1.35.jar" /> -->
    <!--MyBatis3 生成2个实体类，MyBatis3Simple 生成一个实体类(建议使用，代码简洁)   -->

    <context id="DB2Tables"  targetRuntime="MyBatis3Simple" defaultModelType="flat">

        <!-- 这里引入扩展插件 -->
        <plugin type="com.zengtengpeng.generator.plugin.InitSetting" />
        <!-- 去除自动生成的注释 -->
        <!-- <commentGenerator>
            <property name="suppressAllComments" value="true" />
        </commentGenerator> -->
        <commentGenerator type="com.zengtengpeng.generator.plugin.MyCommentGenerator" >
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="${jdbcUrl}"  userId="${user}"
                        password="${password}">
        </jdbcConnection>
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>
        <!-- targetProject:自动生成代码的位置 -->
        <javaModelGenerator targetPackage="${packParent}.bean"
                            targetProject="${parentPath}\src\main\java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <sqlMapGenerator  targetPackage="mybatisMapper"
                          targetProject="${parentPath}\src\main\resources">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>
        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="${packParent}.dao"
                             targetProject="${parentPath}\src\main\java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>
        <!-- tableName:用于自动生成代码的数据库表；domainObjectName:对应于数据库表的javaBean类名 -->
        <#list tables as t>

        <table tableName="${t.tableName}" domainObjectName="${t.beanName}" mapperName="${t.beanName}Dao"   />
        </#list>
    </context>
</generatorConfiguration>