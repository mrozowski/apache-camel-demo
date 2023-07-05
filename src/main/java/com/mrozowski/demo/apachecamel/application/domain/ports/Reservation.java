package com.mrozowski.demo.apachecamel.application.domain.ports;

import com.mrozowski.demo.apachecamel.application.domain.AvailabilityCommand;
import com.mrozowski.demo.apachecamel.application.domain.Day;
import com.mrozowski.demo.apachecamel.application.domain.ReservationCommand;

import java.util.List;

public interface Reservation {

  String reserve(ReservationCommand command);

  List<Day> checkAvailability(AvailabilityCommand availabilityCommand);
}
