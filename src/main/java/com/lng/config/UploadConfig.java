package com.lng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;

import com.lng.tools.CustomMultipartResolver;

/**
 * 配置上传解析器
 * @author wm
 * @Version : 1.0
 * @ModifiedBy : 修改人
 * @date  2019年12月9日 下午12:35:12
 */
@Configuration
public class UploadConfig {

	@Bean
    public MultipartResolver multipartResolver()
    {
        return new CustomMultipartResolver();
    }
}
