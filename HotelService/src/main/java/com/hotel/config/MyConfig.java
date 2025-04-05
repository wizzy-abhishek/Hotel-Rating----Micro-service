package com.hotel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class MyConfig {

    @Bean
    public RestClient restClient(){
        return RestClient.builder().build() ;
    }
}
