package com.mrozowski.demo.apachecamel.io.domain.port;

import com.mrozowski.demo.apachecamel.io.domain.SaveReservationCommand;

public interface ReservationRepository {

  void save(String id, SaveReservationCommand command);
}
