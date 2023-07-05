package com.mrozowski.demo.apachecamel.application.adapter.outgoing;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
class CamelReservationRouting extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    from("direct::storeReservation").process("reservationProcessor");

    from("direct::availability").process("availabilityProcessor");
  }
}
