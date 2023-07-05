package com.mrozowski.demo.apachecamel.application.adapter.incoming

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrozowski.demo.apachecamel.io.domain.SaveReservationCommand
import com.mrozowski.demo.apachecamel.io.domain.port.ReservationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.time.LocalDate

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ActiveProfiles("test")
@ContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ReservationIntegrationSpec extends Specification {

  private static final PATH = "src/test/resources/reservation/test"
  private static final OBJECT_MAPPER = new ObjectMapper()
  private static final LOCALHOST_URI = "http://localhost:"

  @LocalServerPort
  int port

  @Autowired
  private ReservationRepository reservationRepository

  TestRestTemplate restTemplate = new TestRestTemplate()


  def "should reserve room"() {
    given:
    def requestBody = OBJECT_MAPPER.writeValueAsString(Fixture.RESERVATION_REQUEST)
    def conditions = new PollingConditions(timeout: 10, initialDelay: 0.5, factor: 1.25)
    def headers = new HttpHeaders()
    headers.setContentType(MediaType.APPLICATION_JSON)


    when:
    def response = restTemplate.postForObject(
        LOCALHOST_URI + port + "/v1/reserve/room", new HttpEntity<>(requestBody, headers),
        String.class)

    then:

    conditions.eventually {
      assertFileExist("room_1.csv")
      assertFileContainsValidRow("room_1.csv", extractUUID(response), Fixture.NAME, Fixture.SURNAME, Fixture.RESERVATION_DATE_IN, Fixture.RESERVATION_DATE_OUT)
    }
  }

  def "should return room availability for given time"() {
    given:
    def id = UUID.randomUUID().toString()
    def reservation = SaveReservationCommand.builder()
        .name("Marek")
        .surname("Johnson")
        .numberOfDays(3)
        .reservationDate(LocalDate.of(2023, 5, 01))
        .roomId("1")
        .build()
    reservationRepository.createEmptyFiles()
    reservationRepository.save(id, reservation)

    when:
    def jsonResponse = restTemplate.getForObject(
        LOCALHOST_URI + port + "/v1/reserve/available?dateFrom=2023-05-01&dateTo=2023-05-05&roomId=1", String.class)
    def response = OBJECT_MAPPER.readValue(jsonResponse, AvailabilityResponse.class)

    then:
    with(response){
      roomId == "1"
      from == "2023-05-01"
      to == "2023-05-05"
      days.size() == 5
      days.get(0).date == "2023-05-01"
      days.get(0).isOccupied()
      days.get(1).date == "2023-05-02"
      days.get(1).isOccupied()
      days.get(2).date == "2023-05-03"
      days.get(2).isOccupied()
      days.get(3).date == "2023-05-04"
      days.get(3).isAvailable()
      days.get(4).date == "2023-05-05"
      days.get(4).isAvailable()
    }
  }

  def cleanup() {
    reservationRepository.delete()
  }

  private static String extractUUID(String input) {
    def startIndex = input.indexOf('=') + 1
    def endIndex = input.indexOf('}')
    return input.substring(startIndex, endIndex)
  }

  private void assertFileExist(String fileName) {
    assert new File(PATH + "/" + fileName).exists()
  }

  private void assertFileContainsValidRow(String fileName, String id, String name, String surname, String checkInDate, String checkOutDate) {
    def file = new File(PATH + "/" + fileName)
    def lines = file.readLines()
    assert lines.size() > 1
    def values = lines.last().split(",")
    assert values[0] == id
    assert values[1] == name
    assert values[2] == surname
    assert values[3] == checkInDate
    assert values[4] == checkOutDate
  }
}
