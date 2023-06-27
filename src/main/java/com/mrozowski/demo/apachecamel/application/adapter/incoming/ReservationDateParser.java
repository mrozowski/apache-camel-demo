package com.mrozowski.demo.apachecamel.application.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@Component
@RequiredArgsConstructor
class ReservationDateParser {

  private final DateTimeFormatter reservationDateFormatter;

  LocalDate toLocalDate(String date) {
    try {
      return LocalDate.parse(date, reservationDateFormatter);
    } catch (DateTimeParseException e) {
      log.error("Incorrect date format [{}]", date, e);
      throw e;
    }
  }
}
