package com.example.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.publisher"})
@EnableScheduling
public class PublisherApplication {
	public static void main(String[] args) {
		SpringApplication.run(PublisherApplication.class, args);
	}
}
