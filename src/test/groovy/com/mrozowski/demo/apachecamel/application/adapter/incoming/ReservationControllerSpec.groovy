package com.mrozowski.demo.apachecamel.application.adapter.incoming

import com.mrozowski.demo.apachecamel.application.domain.AvailabilityCommand
import com.mrozowski.demo.apachecamel.application.domain.Day
import com.mrozowski.demo.apachecamel.application.domain.ReservationCommand
import com.mrozowski.demo.apachecamel.application.domain.ReservationFacade
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.format.DateTimeParseException

class ReservationControllerSpec extends Specification {

    private static final String RESERVATION_ID = "reservation_id"
    def reservationFacade = Mock(ReservationFacade)
    def reservationDateParser = Mock(ReservationDateParser)

    @Subject
    def underTest = new ReservationController(reservationDateParser, reservationFacade)

    def "reserve should return OK with the reservation ID"() {
        given:
        def request = new ReservationRequest(name: "John", surname: "Doe", room: "123", days: 3, date: "03.05.2023")
        def dateTime = LocalDate.of(2023, 5, 3);
        def command = ReservationCommand.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .roomId(request.getRoom())
                .reservationDate(dateTime)
                .numberOfDays(request.getDays())
                .build()

        when:
        def response = underTest.reserve(request)

        then:
        1 * reservationDateParser.toLocalDate("03.05.2023") >> dateTime
        1 * reservationFacade.reserve(command) >> RESERVATION_ID
        response.statusCode == HttpStatus.OK
        response.body == RESERVATION_ID
    }

    def "should check availability"() {
        given:
        def days = [
            Day.of("2023-01-01", true),
            Day.of("2023-01-02", false)
        ]
        def availabilityCommand = new AvailabilityCommand("2023-01-01","2023-01-03", "123")
        def expectedResponse = new AvailabilityResponse(
            from: "2023-01-01",
            to: "2023-01-03",
            roomId: "123",
            days: [
                new AvailabilityResponse.DayAvailability(date: "2023-01-01", available: true),
                new AvailabilityResponse.DayAvailability(date: "2023-01-02", available: false)
            ])

        when:
        def responseEntity = underTest.isAvailable("2023-01-01", "2023-01-03", "123")

        then:
        1 * reservationFacade.checkAvailable(availabilityCommand) >> days
        responseEntity.statusCode == HttpStatus.OK
        responseEntity.body == expectedResponse
    }

    def "test handleDateTimeParseException"() {
        given:
        def ex = new DateTimeParseException("Invalid date format", "05.01.2023", 2)

        when:
        def responseEntity = underTest.handleIDateTimeParseException(ex)

        then:
        responseEntity.statusCode == HttpStatus.BAD_REQUEST
        responseEntity.body == "Invalid date format"
    }

    def "test handleIllegalArgumentException"() {
        def message = "Conflict"
        given:
        def ex = new IllegalArgumentException(message)

        when:
        def responseEntity = underTest.handleIIllegalArgumentException(ex)

        then:
        responseEntity.statusCode == HttpStatus.CONFLICT
        responseEntity.body == message
    }
}
