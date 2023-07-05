package com.mrozowski.demo.apachecamel.io.adapter.outgoing

import spock.lang.Specification

import java.time.LocalDate

class AvailabilityInformationCreatorSpec extends Specification {

  def "should create AvailabilityInformation object"() {
    given:
    def from = LocalDate.of(2023, 1, 1)
    def to = LocalDate.of(2023, 1, 3)
    def roomId = "123"

    when:
    def availabilityCreator = AvailabilityInformationCreator.ofDays(from, to, roomId)
    availabilityCreator.setAvailabilityFor(LocalDate.of(2023, 1, 2), LocalDate.of(2023, 1, 4))
    def availabilityInformation = availabilityCreator.create()

    then:
    with(availabilityInformation){
      it.roomId() == "123"
      it.dayInformationList().size() == 3
      it.dayInformationList()[0].date() == "2023-01-01"
      it.dayInformationList()[0].isAvailable()
      it.dayInformationList()[1].date() == "2023-01-02"
      !it.dayInformationList()[1].isAvailable()
      it.dayInformationList()[2].date() == "2023-01-03"
      !it.dayInformationList()[2].isAvailable()
    }
  }
}
