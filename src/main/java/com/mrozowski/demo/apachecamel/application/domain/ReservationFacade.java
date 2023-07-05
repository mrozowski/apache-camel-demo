package com.mrozowski.demo.apachecamel.application.domain;

import com.mrozowski.demo.apachecamel.application.domain.ports.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationFacade {

  private final Reservation reservation;

  public String reserve(ReservationCommand command) {
    log.info("Reservation received");
    var dateTo = command.reservationDate().plusDays(command.numberOfDays()).toString();
    var dateFrom = command.reservationDate().toString();
    var days = reservation.checkAvailability(AvailabilityCommand.builder()
        .roomId(command.roomId())
        .dateFrom(dateFrom)
        .dateTo(dateTo)
        .build());
    if (days.stream().anyMatch(Day::isOccupied)) {
      log.warn("Could not reserve room [{}] for date [{} - {}]", command.roomId(), dateFrom, dateTo);
      throw new IllegalArgumentException(
          "Room with id [%s] is occupied for date [%s - %s]".formatted(command.roomId(), dateFrom, dateTo));
    }
    return reservation.reserve(command);
  }

  public List<Day> checkAvailable(AvailabilityCommand availabilityCommand) {
    log.info("Checking availability for room [{}], date: [{} - {}]",
        availabilityCommand.roomId(),
        availabilityCommand.dateFrom(),
        availabilityCommand.dateTo());
    return reservation.checkAvailability(availabilityCommand);

  }
}
