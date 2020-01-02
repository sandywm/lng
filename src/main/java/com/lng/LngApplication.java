package com.lng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
//打war时集成
public class LngApplication  /**extends SpringBootServletInitializer*/{
	private static Logger logger = LoggerFactory.getLogger(LngApplication.class);
	
	//打war时重写
//	@Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(LngApplication.class);
//    }
	
	public static void main(String[] args) {
		SpringApplication.run(LngApplication.class, args);
		logger.info("启动成功");
	}

}
