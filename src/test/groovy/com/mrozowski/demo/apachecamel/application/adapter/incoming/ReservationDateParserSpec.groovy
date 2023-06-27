package com.mrozowski.demo.apachecamel.application.adapter.incoming

import spock.lang.Specification
import spock.lang.Subject

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ReservationDateParserSpec extends Specification {

    def reservationDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @Subject
    def reservationDateParser = new ReservationDateParser(reservationDateFormatter)

    def "should parse the valid date string"() {
        given:
        def validDate = "2023-06-30"

        when:
        def result = reservationDateParser.toLocalDate(validDate)

        then:
        result != null
        result.year == 2023
        result.monthValue == 6
        result.dayOfMonth == 30
    }

    def "should throw DateTimeParseException for invalid date string"() {
        given:
        def invalidDate = "30-12-2023"

        when:
        reservationDateParser.toLocalDate(invalidDate)

        then:
        thrown(DateTimeParseException)
    }
}
