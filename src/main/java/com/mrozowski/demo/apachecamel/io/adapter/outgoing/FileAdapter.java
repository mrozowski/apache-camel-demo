package com.mrozowski.demo.apachecamel.io.adapter.outgoing;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.mrozowski.demo.apachecamel.io.domain.FileOperationException;
import com.mrozowski.demo.apachecamel.io.domain.SaveReservationCommand;
import com.mrozowski.demo.apachecamel.io.domain.port.ReservationRepository;
import com.mrozowski.demo.apachecamel.io.infrastructure.FileProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
@RequiredArgsConstructor
class FileAdapter implements ReservationRepository {

  private final FileProperties fileProperties;
  private final CsvMapper csvMapper;

  @Override
  public void save(String id, SaveReservationCommand command) {
    var filePath = toFilePath(command.roomId());
    var content = ReservationCsvFile.builder()
        .id(id)
        .name(command.name())
        .surname(command.surname())
        .checkInDate(command.reservationDate().toString())
        .checkOutDate(command.reservationDate().plusDays((command.numberOfDays())).toString())
        .build();

    appendToFile(filePath, content);
  }

  @PostConstruct
  void createFilesIfNotExist() {
    log.info("Initializing csv files if not exist in path: [{}]", fileProperties.roomReservationPath());
    fileProperties.rooms().stream()
        .map(this::toFilePath)
        .forEach(this::createFileWithHeaderIfNotExist);
  }

  private void createFileWithHeaderIfNotExist(String path) {
    try {
      var outputFile = new File(path);
      if (!outputFile.exists()) {
        var csvSchema = csvMapper.schemaFor(ReservationCsvFile.class).withHeader();
        var writer = csvMapper
            .writer(csvSchema)
            .withDefaultPrettyPrinter();
        writer.writeValue(outputFile, null);
      }
    } catch (IOException e) {
      log.error("File error", e);
      throw new FileOperationException("Failed to create file [" + path + "]", e);
    }
  }

  private String toFilePath(String roomNumber) {
    return String.format("%s/room_%s.csv", fileProperties.roomReservationPath(), roomNumber);
  }

  private void appendToFile(String filePath, ReservationCsvFile content) {
    try (var file = new FileOutputStream(filePath, true)) {
      var csvSchema = csvMapper.schemaFor(ReservationCsvFile.class).withoutQuoteChar() ;
      var writer = csvMapper
          .writer(csvSchema)
          .withDefaultPrettyPrinter();
      writer.writeValue(file, content);
    } catch (IOException e) {
      throw new FileOperationException("Failed to save reservation to file [" + filePath + "]", e);
    }
  }
}
