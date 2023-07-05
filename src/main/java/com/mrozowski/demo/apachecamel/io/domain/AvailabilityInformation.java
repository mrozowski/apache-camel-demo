package com.mrozowski.demo.apachecamel.io.domain;

import java.util.List;


public record AvailabilityInformation(List<DayInformation> dayInformationList, String roomId) {

  public static record DayInformation(String date, boolean isAvailable) {
  }
}
