package com.mrozowski.demo.apachecamel.io.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IoFacade {

  private final ReservationService reservationService;

  public String save(SaveReservationCommand command) {
    log.debug("Received data to save [{}]", command);
    return reservationService.save(command);
  }

  public AvailabilityInformation checkAvailability(AvailabilityCheckCommand availabilityCheckCommand) {
    log.debug("Received request to check availability [{}]", availabilityCheckCommand);
    return reservationService.checkAvailability(availabilityCheckCommand);
  }
}
