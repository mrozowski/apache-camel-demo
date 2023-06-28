package com.mrozowski.demo.apachecamel.io.adapter.outgoing;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "surname", "checkInDate", "checkOutDate"})
class ReservationCsvFile {
  private String id;
  private String name;
  private String surname;
  private String checkInDate;
  private String checkOutDate;
}