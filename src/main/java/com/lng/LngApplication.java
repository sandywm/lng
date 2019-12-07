package com.lng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LngApplication {
	private static Logger logger = LoggerFactory.getLogger(LngApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(LngApplication.class, args);
		logger.info("启动成功");
	}

}
