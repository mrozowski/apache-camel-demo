package com.mrozowski.demo.apachecamel.application.adapter.outgoing;

import com.mrozowski.demo.apachecamel.application.domain.ReservationCommand;
import com.mrozowski.demo.apachecamel.application.domain.ports.Reservation;
import lombok.RequiredArgsConstructor;
import org.apache.camel.FluentProducerTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
class CamelReservationProducer implements Reservation {

  private final FluentProducerTemplate producerTemplate;

  @Override
  public String reserve(ReservationCommand command) {
    return producerTemplate
        .to("direct::storeReservation")
        .withBody(toReservationMap(command))
        .request(String.class);
  }

  private Map<String, Object> toReservationMap(ReservationCommand command) {
    return Map.ofEntries(
        Map.entry("name", command.name()),
        Map.entry("surname", command.surname()),
        Map.entry("room", command.roomId()),
        Map.entry("date", command.reservationDate()),
        Map.entry("days", command.numberOfDays()));
  }
}
