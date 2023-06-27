package com.mrozowski.demo.apachecamel.application.domain;

import com.mrozowski.demo.apachecamel.application.domain.ports.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationFacade {

  private final Reservation reservation;

  public String reserve(ReservationCommand command) {
    log.info("Reservation received");
    return reservation.reserve(command);
  }
}
