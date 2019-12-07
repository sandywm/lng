package com.lng.config;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration//配置404,5xx页面
public class ErrorPageConfig {

	@Bean
	//java8 lambda写法
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
		return (factory -> {
			ErrorPage[] errorPages = new ErrorPage[]{
					new ErrorPage(HttpStatus.NOT_FOUND, "/404.html"),
					new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/5xx.html"),
					new ErrorPage(Throwable.class, "/5xx.html"),
			};
            factory.addErrorPages(errorPages);
        });
	}
}
