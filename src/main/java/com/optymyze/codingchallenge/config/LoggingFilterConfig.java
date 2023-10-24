package com.optymyze.codingchallenge.config;

import ch.qos.logback.access.servlet.TeeFilter;
import com.optymyze.codingchallenge.constants.URI;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class LoggingFilterConfig {

    @Bean(name = "TeeFilter")
    public Filter teeFilter() {
        return new ch.qos.logback.access.servlet.TeeFilter();
    }
}
