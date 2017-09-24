package com.cus.metime.salon.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.cus.metime.salon")
public class FeignConfiguration {

}
