package com.mrozowski.demo.apachecamel.io.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "reservation.storage")
public record FileProperties(String roomReservationPath, List<String> rooms) {
}
