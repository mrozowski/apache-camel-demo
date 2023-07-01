package com.mrozowski.demo.apachecamel.io.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(FileProperties.class)
public class FileConfiguration {

  @Bean
  CsvMapper csvMapper() {
    return new CsvMapper();
  }

  @Bean
  @Primary
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
