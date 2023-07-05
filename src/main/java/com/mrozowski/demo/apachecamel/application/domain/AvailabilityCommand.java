package com.mrozowski.demo.apachecamel.application.domain;

import lombok.Builder;

@Builder
public record AvailabilityCommand(String dateFrom, String dateTo, String roomId) {
}
