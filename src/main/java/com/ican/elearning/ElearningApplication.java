package com.ican.elearning;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//定時任務註解
@EnableScheduling
@SpringBootApplication
public class ElearningApplication {
	public static void main(String[] args) {
		SpringApplication.run(ElearningApplication.class, args);
	}

}
