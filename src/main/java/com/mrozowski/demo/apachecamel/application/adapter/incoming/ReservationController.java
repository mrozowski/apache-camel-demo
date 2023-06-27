package com.mrozowski.demo.apachecamel.application.adapter.incoming;

import com.mrozowski.demo.apachecamel.application.domain.ReservationCommand;
import com.mrozowski.demo.apachecamel.application.domain.ReservationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;

@Slf4j
@RestController
@RequestMapping("v1/reserve")
@RequiredArgsConstructor
class ReservationController {

  private final ReservationDateParser dateParser;
  private final ReservationFacade reservationFacade;

  @PostMapping("/room")
  ResponseEntity<String> reserve(@RequestBody ReservationRequest request) {
    return ResponseEntity.ok().body(reservationFacade.reserve(toCommand(request)));
  }

  private ReservationCommand toCommand(ReservationRequest request) {
    return ReservationCommand.builder()
        .name(request.getName())
        .surname(request.getSurname())
        .roomId(request.getRoom())
        .reservationDate(dateParser.toLocalDate(request.getDate()))
        .numberOfDays(request.getDays())
        .build();
  }

  @ExceptionHandler(DateTimeParseException.class)
  ResponseEntity<String> handleIDateTimeParseException(DateTimeParseException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format");
  }
}
