package com.kalex.hosdoc_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableDiscoveryClient
@CrossOrigin("*")
@EntityScan("com.kalex.hosdoc_auth.entity")
public class HosdocAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(HosdocAuthApplication.class, args);
	}

}
