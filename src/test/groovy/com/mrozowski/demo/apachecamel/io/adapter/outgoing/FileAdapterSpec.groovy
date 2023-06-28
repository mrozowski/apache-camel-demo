package com.mrozowski.demo.apachecamel.io.adapter.outgoing

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.mrozowski.demo.apachecamel.io.domain.SaveReservationCommand
import com.mrozowski.demo.apachecamel.io.infrastructure.FileProperties
import org.apache.tomcat.util.http.fileupload.FileUtils
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class FileAdapterSpec extends Specification {

  private static final String PATH = "src/test/resources/reservation"
  def properties = new FileProperties(PATH, ["1", "2"])
  def csvMapper = new CsvMapper()

  @Subject
  def underTest = new FileAdapter(properties, csvMapper)

  def "should create files if they do not exist and save reservation"() {
    given:
    def id = "someId"
    def reservation = SaveReservationCommand.builder()
        .name("Marek")
        .surname("Johnson")
        .numberOfDays(2)
        .reservationDate(LocalDate.of(2023, 5, 15))
        .roomId("1")
        .build()

    when:
    underTest.createFilesIfNotExist()

    and:
    underTest.save(id, reservation)

    then:
    assertFileExist("room_1.csv")
    assertFileExist("room_2.csv")
    assertFileContainsRow("room_1.csv", "someId,Marek,Johnson,2023-05-15,2023-05-17")
  }

  def cleanup() {
    println "Cleaning up test resources"
    FileUtils.cleanDirectory(new File(PATH))
    println PATH + " directory cleaned"
  }

  void assertFileExist(String fileName) {
    assert new File(PATH + "/" + fileName).exists()
  }

  void assertFileContainsRow(String fileName, String row) {
    def file = new File(PATH + "/" + fileName)
    def lines = file.readLines()
    assert lines.size() == 2
    assert lines.get(1) == row
  }
}
