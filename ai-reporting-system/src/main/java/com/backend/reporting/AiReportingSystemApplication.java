package com.backend.reporting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AiReportingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiReportingSystemApplication.class, args);
	}

}
