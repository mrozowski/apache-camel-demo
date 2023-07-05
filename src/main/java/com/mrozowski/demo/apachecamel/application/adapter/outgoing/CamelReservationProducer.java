package com.mrozowski.demo.apachecamel.application.adapter.outgoing;

import com.mrozowski.demo.apachecamel.application.domain.AvailabilityCommand;
import com.mrozowski.demo.apachecamel.application.domain.Day;
import com.mrozowski.demo.apachecamel.application.domain.ReservationCommand;
import com.mrozowski.demo.apachecamel.application.domain.ports.Reservation;
import com.mrozowski.demo.apachecamel.common.model.camel.CamelDaysAvailabilityRequestMessage;
import com.mrozowski.demo.apachecamel.common.model.camel.CamelDaysAvailabilityResponseMessage;
import lombok.RequiredArgsConstructor;
import org.apache.camel.FluentProducerTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
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

  @Override
  public List<Day> checkAvailability(AvailabilityCommand availabilityCommand) {
    var response = producerTemplate
        .to("direct::availability")
        .withBody(toAvailabilityRequestMessage(availabilityCommand))
        .request(CamelDaysAvailabilityResponseMessage.class);
    return toDayList(response);
  }

  private List<Day> toDayList(CamelDaysAvailabilityResponseMessage response) {
    return response.dayAvailabilityList().stream()
        .map(dayAvailability -> Day.of(dayAvailability.day(), dayAvailability.isAvailable()))
        .toList();
  }

  private Map<String, Object> toAvailabilityMap(AvailabilityCommand availabilityCommand) {
    return Map.ofEntries(
        Map.entry("from", availabilityCommand.dateFrom()),
        Map.entry("to", availabilityCommand.dateTo()),
        Map.entry("room", availabilityCommand.roomId())
    );
  }

  private CamelDaysAvailabilityRequestMessage toAvailabilityRequestMessage(AvailabilityCommand availabilityCommand) {
    return CamelDaysAvailabilityRequestMessage.builder()
        .from(availabilityCommand.dateFrom())
        .to(availabilityCommand.dateTo())
        .roomId(availabilityCommand.roomId())
        .build();
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
