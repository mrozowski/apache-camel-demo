package com.mrozowski.demo.apachecamel.application.domain;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ReservationCommand(String name,
                                 String surname,
                                 String roomId,
                                 LocalDate reservationDate,
                                 int numberOfDays) {
}
