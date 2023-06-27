package com.mrozowski.demo.apachecamel.application.infrastructure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
@EnableConfigurationProperties(ApplicationDateProperties.class)
class ApplicationConfiguration {

  @Bean
  DateTimeFormatter reservationDateFormatter(ApplicationDateProperties dateProperties) {
    return DateTimeFormatter.ofPattern(dateProperties.dateFormat(), Locale.ENGLISH);
  }
}
