package com.mrozowski.demo.apachecamel.common.model.camel;

import lombok.Builder;

@Builder
public record CamelDaysAvailabilityRequestMessage(String from, String to, String roomId) {
}
