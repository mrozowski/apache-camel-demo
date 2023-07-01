package com.mrozowski.demo.apachecamel.io.adapter.incoming;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mrozowski.demo.apachecamel.io.domain.IoFacade;
import com.mrozowski.demo.apachecamel.io.domain.SaveReservationCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Service("reservationProcessor")
@RequiredArgsConstructor
public class CamelReservationProcessor implements Processor {

  private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  private final IoFacade ioFacade;

  @Override
  public void process(Exchange exchange){
    log.info("Received reservation to process");
    var body = OBJECT_MAPPER.convertValue(
        exchange.getMessage().getBody(),
        new TypeReference<Map<String, Object>>() {}
    );
    var reservationId = ioFacade.save(toCommand(body));
    exchange.getMessage().setBody(Map.of("id", reservationId));
  }

  private SaveReservationCommand toCommand(Map<String, Object> body) {
    return SaveReservationCommand.builder()
        .name((String) body.get("name"))
        .surname((String) body.get("surname"))
        .roomId((String) body.get("room"))
        .reservationDate(toLocalDate((String) body.get("date")))
        .numberOfDays((int) body.get("days"))
        .build();
  }

  private LocalDate toLocalDate(String date) {
    return LocalDate.parse(date);
  }
}
