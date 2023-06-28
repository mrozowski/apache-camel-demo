package com.mrozowski.demo.apachecamel.io.domain;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SaveReservationCommand(String name,
                                     String surname,
                                     String roomId,
                                     LocalDate reservationDate,
                                     int numberOfDays) {
}
