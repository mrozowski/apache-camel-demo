package com.mrozowski.demo.apachecamel.application.domain.ports;

import com.mrozowski.demo.apachecamel.application.domain.ReservationCommand;

public interface Reservation {

  String reserve(ReservationCommand command);
}
