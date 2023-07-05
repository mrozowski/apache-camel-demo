package com.mrozowski.demo.apachecamel.application.adapter.incoming;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class AvailabilityResponse {

  private String roomId;
  private String from;
  private String to;
  private List<DayAvailability> days;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor(staticName = "of")
  static class DayAvailability {
    private String date;
    private boolean available;

    boolean isOccupied() {
      return !available;
    }
  }
}
