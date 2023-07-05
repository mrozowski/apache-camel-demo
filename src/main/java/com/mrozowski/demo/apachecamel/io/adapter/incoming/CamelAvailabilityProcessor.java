package com.mrozowski.demo.apachecamel.io.adapter.incoming;

import com.mrozowski.demo.apachecamel.common.model.camel.CamelDaysAvailabilityRequestMessage;
import com.mrozowski.demo.apachecamel.common.model.camel.CamelDaysAvailabilityResponseMessage;
import com.mrozowski.demo.apachecamel.io.domain.AvailabilityCheckCommand;
import com.mrozowski.demo.apachecamel.io.domain.AvailabilityInformation;
import com.mrozowski.demo.apachecamel.io.domain.IoFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Slf4j
@Service("availabilityProcessor")
@RequiredArgsConstructor
public class CamelAvailabilityProcessor implements Processor {

  private final IoFacade ioFacade;

  @Override
  public void process(Exchange exchange) throws Exception {
    var body = exchange.getMessage().getBody(CamelDaysAvailabilityRequestMessage.class);
    var availability = ioFacade.checkAvailability(toAvailabilityCheckCommand(body));
    var response = toAvailabilityResponseMessage(availability);
    exchange.getMessage().setBody(response);
  }

  private CamelDaysAvailabilityResponseMessage toAvailabilityResponseMessage(AvailabilityInformation availability) {
    return CamelDaysAvailabilityResponseMessage.of(availability.dayInformationList().stream()
        .map(day -> CamelDaysAvailabilityResponseMessage.DayAvailability.of(day.date(), day.isAvailable()))
        .toList());
  }

  private AvailabilityCheckCommand toAvailabilityCheckCommand(CamelDaysAvailabilityRequestMessage message) {
    return AvailabilityCheckCommand.builder().from(message.from()).to(message.to()).roomId(message.roomId()).build();
  }
}
