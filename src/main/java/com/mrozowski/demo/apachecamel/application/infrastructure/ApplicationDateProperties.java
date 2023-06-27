package com.mrozowski.demo.apachecamel.application.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "reservation.date")
public record ApplicationDateProperties(String dateFormat) {

}
