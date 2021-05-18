package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import normal.normal;
// import normal.conSql;

//@表示注解，这个表明是基于SpringBoot框架的项目
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// 启动项目
		SpringApplication.run(DemoApplication.class, args);

		// 测试是否能在命令行进行输出
//		 normal.work();

		// 测试数据库连接
//		 conSql.connectTest();
	}
}
