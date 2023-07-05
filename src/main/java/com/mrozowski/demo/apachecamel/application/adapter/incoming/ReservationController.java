package com.mrozowski.demo.apachecamel.application.adapter.incoming;

import com.mrozowski.demo.apachecamel.application.domain.AvailabilityCommand;
import com.mrozowski.demo.apachecamel.application.domain.Day;
import com.mrozowski.demo.apachecamel.application.domain.ReservationCommand;
import com.mrozowski.demo.apachecamel.application.domain.ReservationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/reserve")
@RequiredArgsConstructor
class ReservationController {

  private final ReservationDateParser dateParser;
  private final ReservationFacade reservationFacade;

  @PostMapping("/room")
  ResponseEntity<String> reserve(@RequestBody ReservationRequest request) {
    log.info("Receive request to reserve room [{}]", request.getRoom());
    return ResponseEntity.ok().body(reservationFacade.reserve(toReservationCommand(request)));
  }

  @GetMapping("/available")
  ResponseEntity<AvailabilityResponse> isAvailable(
      @RequestParam String dateFrom, @RequestParam String dateTo, @RequestParam String roomId) {
    log.info("Receive request to check availability for room [{}], and date [{} - {}]", roomId, dateFrom, dateTo);
    var days = reservationFacade.checkAvailable(toAvailabilityCommand(dateFrom, dateTo, roomId));
    return ResponseEntity.ok().body(toAvailabilityResponse(days, dateFrom, dateTo, roomId));
  }

  private AvailabilityResponse toAvailabilityResponse(List<Day> days, String from, String to, String roomId) {
    return AvailabilityResponse.builder()
        .from(from)
        .to(to)
        .roomId(roomId)
        .days(days.stream().map(ReservationController::toDayAvailability).toList())
        .build();
  }

  private AvailabilityCommand toAvailabilityCommand(String dateFrom, String dateTo, String roomId) {
    return AvailabilityCommand.builder()
        .dateFrom(dateFrom)
        .dateTo(dateTo)
        .roomId(roomId)
        .build();
  }

  private ReservationCommand toReservationCommand(ReservationRequest request) {
    return ReservationCommand.builder()
        .name(request.getName())
        .surname(request.getSurname())
        .roomId(request.getRoom())
        .reservationDate(dateParser.toLocalDate(request.getDate()))
        .numberOfDays(request.getDays())
        .build();
  }

  private static AvailabilityResponse.DayAvailability toDayAvailability(Day day) {
    return AvailabilityResponse.DayAvailability.of(day.date(), day.isAvailable());
  }


  @ExceptionHandler(DateTimeParseException.class)
  ResponseEntity<String> handleIDateTimeParseException(DateTimeParseException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format");
  }

  @ExceptionHandler(IllegalArgumentException.class)
  ResponseEntity<String> handleIIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }
}
