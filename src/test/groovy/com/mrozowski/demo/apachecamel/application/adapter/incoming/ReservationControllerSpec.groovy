package com.mrozowski.demo.apachecamel.application.adapter.incoming

import com.mrozowski.demo.apachecamel.application.domain.ReservationCommand
import com.mrozowski.demo.apachecamel.application.domain.ReservationFacade
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class ReservationControllerSpec extends Specification {

    private static final String RESERVATION_ID = "reservation_id"
    def reservationFacade = Mock(ReservationFacade)
    def reservationDateParser = Mock(ReservationDateParser)

    @Subject
    def reservationController = new ReservationController(reservationDateParser, reservationFacade)

    def "reserve should return OK with the reservation ID"() {
        given:
        def request = new ReservationRequest(name: "John", surname: "Doe", room: 123, days: 3, date: "03.05.2023")
        def dateTime = LocalDate.of(2023, 5, 3);
        def command = ReservationCommand.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .roomId(request.getRoom())
                .reservationDate(dateTime)
                .numberOfDays(request.getDays())
                .build()

        when:
        def response = reservationController.reserve(request)

        then:
        1 * reservationDateParser.toLocalDate("03.05.2023") >> dateTime
        1 * reservationFacade.reserve(command) >> RESERVATION_ID
        response.statusCode == HttpStatus.OK
        response.body == RESERVATION_ID
    }
}
