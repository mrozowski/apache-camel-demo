package com.mrozowski.demo.apachecamel.io.adapter.outgoing;

import com.mrozowski.demo.apachecamel.io.domain.AvailabilityInformation;
import com.mrozowski.demo.apachecamel.io.domain.AvailabilityInformation.DayInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@RequiredArgsConstructor
class AvailabilityInformationCreator {

  private final List<DayInfoHolder> days;
  private final String roomId;

  static AvailabilityInformationCreator ofDays(LocalDate from, LocalDate to, String roomId) {
    long between = DAYS.between(from, to);
    var days = new ArrayList<DayInfoHolder>();
    for (int i = 0; i < between + 1; i++) {
      days.add(DayInfoHolder.ofDate(DAYS.addTo(from, i)));
    }
    return new AvailabilityInformationCreator(days, roomId);
  }

  void setAvailabilityFor(LocalDate checkIn, LocalDate checkOut) {
    checkIn.datesUntil(checkOut)
        .filter(reservedDate -> reservedDate.isAfter(days.get(0).date.minusDays(1)))
        .forEach(reservedDate -> days.stream()
            .filter(day -> day.date.isEqual(reservedDate))
            .findFirst()
            .ifPresent(day -> day.setAvailable(false)));
  }

  AvailabilityInformation create() {
    var daysInformation = days.stream().map(day -> new DayInformation(day.date.toString(), day.isAvailable)).toList();
    return new AvailabilityInformation(daysInformation, roomId);
  }

  @Data
  @AllArgsConstructor
  private static class DayInfoHolder {
    private LocalDate date;
    private boolean isAvailable;

    static DayInfoHolder ofDate(LocalDate date) {
      return new DayInfoHolder(date, true);
    }
  }
}
