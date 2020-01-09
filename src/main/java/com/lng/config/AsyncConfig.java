package com.lng.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 多个定时器同步进行配置
 * @author Administrator
 *
 */
@Configuration
@EnableAsync//开启异步事件的支持
public class AsyncConfig {

	private int corePoolSize = 10;
	private int maxPoolSize = 200;
	private int queueCapacity = 10;
	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.initialize();
		return executor;
	}
	
}
