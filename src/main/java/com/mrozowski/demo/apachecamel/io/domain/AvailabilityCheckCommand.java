package com.mrozowski.demo.apachecamel.io.domain;

import lombok.Builder;

@Builder
public record AvailabilityCheckCommand(String from, String to, String roomId) {
}
