package com.mrozowski.demo.apachecamel.io.domain.port;

import com.mrozowski.demo.apachecamel.io.domain.AvailabilityCheckCommand;
import com.mrozowski.demo.apachecamel.io.domain.AvailabilityInformation;
import com.mrozowski.demo.apachecamel.io.domain.SaveReservationCommand;

public interface ReservationRepository {

  void save(String id, SaveReservationCommand command);

  AvailabilityInformation availability(AvailabilityCheckCommand command);

  void createEmptyFiles();

  void delete();
}
