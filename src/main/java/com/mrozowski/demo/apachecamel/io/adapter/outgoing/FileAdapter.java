package com.mrozowski.demo.apachecamel.io.adapter.outgoing;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.mrozowski.demo.apachecamel.io.domain.AvailabilityCheckCommand;
import com.mrozowski.demo.apachecamel.io.domain.AvailabilityInformation;
import com.mrozowski.demo.apachecamel.io.domain.FileOperationException;
import com.mrozowski.demo.apachecamel.io.domain.SaveReservationCommand;
import com.mrozowski.demo.apachecamel.io.domain.port.ReservationRepository;
import com.mrozowski.demo.apachecamel.io.infrastructure.FileProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

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
        .status(ReservationCsvFile.Status.CONFIRMED)
        .build();

    appendToFile(filePath, content);
  }

  @Override
  public AvailabilityInformation availability(AvailabilityCheckCommand command) {
    var from = LocalDate.parse(command.from());
    var to = LocalDate.parse(command.to());
    var informationCreator = AvailabilityInformationCreator.ofDays(from, to, command.roomId());

    try (var iterator = getIterator(toFilePath(command.roomId()))) {
      while (iterator.hasNext()) {
        ReservationCsvFile row = iterator.next();
        if (row.getStatus() != ReservationCsvFile.Status.CONFIRMED) {
          continue;
        }
        var checkIn = LocalDate.parse(row.getCheckInDate());
        var checkOut = LocalDate.parse(row.getCheckOutDate());
        if (checkIn.isBefore(to) && checkOut.isAfter(from)) {
          informationCreator.setAvailabilityFor(checkIn, checkOut);
        }
      }
      return informationCreator.create();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void createEmptyFiles() {
    createFilesIfNotExist();
  }

  @Override
  public void delete() {
    log.warn("Deleting csv files from directory [{}]", fileProperties.roomReservationPath());
    try {
      FileUtils.cleanDirectory(new File(fileProperties.roomReservationPath()));
      log.info("Files deleted successfully");
    } catch (IOException e) {
      log.error("failed to delete files");
      throw new RuntimeException(e);
    }
  }

  private MappingIterator<ReservationCsvFile> getIterator(String filePath) throws IOException {
    var csvSchema = csvMapper.schemaFor(ReservationCsvFile.class).withHeader();
    var reader = csvMapper.readerFor(ReservationCsvFile.class).with(csvSchema);
    return reader.readValues(new File(filePath));
  }

  @PostConstruct
  void createFilesIfNotExist() {
    log.info("Initializing csv files if not exist in path: [{}]", fileProperties.roomReservationPath());
    createDirectoryIfNotExist();
    fileProperties.rooms().stream()
        .map(this::toFilePath)
        .forEach(this::createFileWithHeaderIfNotExist);
  }

  private void createDirectoryIfNotExist() {
    var directory = new File(fileProperties.roomReservationPath());
    if (!directory.exists()) {
      log.info("Directory [{}] does not exist. Creating directory...", fileProperties.roomReservationPath());
      var isDirCreated = directory.mkdirs();
      log.info("Directory [{}] created", fileProperties.roomReservationPath());
    }
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
      var csvSchema = csvMapper.schemaFor(ReservationCsvFile.class).withoutQuoteChar();
      var writer = csvMapper
          .writer(csvSchema)
          .withDefaultPrettyPrinter();
      writer.writeValue(file, content);
    } catch (IOException e) {
      throw new FileOperationException("Failed to save reservation to file [" + filePath + "]", e);
    }
  }
}
