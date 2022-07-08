package controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyApplication {

	public static void main(String[] args) {
		// 启动项目
		SpringApplication.run(MyApplication.class, args);

	}
}