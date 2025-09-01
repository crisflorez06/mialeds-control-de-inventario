package com.mialeds.Config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
public class WebConfig {

        // Activar el filtro para soportar métodos PUT y DELETE en formularios
    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> methodFilter() {
        FilterRegistrationBean<HiddenHttpMethodFilter> filterRegistrationBean = new FilterRegistrationBean<>(new HiddenHttpMethodFilter());
        return filterRegistrationBean;
    }
}
