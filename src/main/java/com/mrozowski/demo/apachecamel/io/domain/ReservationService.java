package com.mrozowski.demo.apachecamel.io.domain;

import com.mrozowski.demo.apachecamel.io.domain.port.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class ReservationService {

  private final ReservationRepository reservationRepository;
  String save(SaveReservationCommand command) {

    var id = UUID.randomUUID().toString();
    reservationRepository.save(id, command);
    return id;
  }
}
