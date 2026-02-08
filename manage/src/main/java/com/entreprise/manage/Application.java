// src/main/java/com/entreprise/manage/Application.java
package com.entreprise.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.entreprise.manage")
@EnableJpaRepositories(basePackages = "com.entreprise.manage")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}