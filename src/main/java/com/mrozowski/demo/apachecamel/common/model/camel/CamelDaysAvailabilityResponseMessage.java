package com.mrozowski.demo.apachecamel.common.model.camel;

import java.util.List;

public record CamelDaysAvailabilityResponseMessage(List<DayAvailability> dayAvailabilityList) {

  public static CamelDaysAvailabilityResponseMessage of(List<DayAvailability> dayAvailabilityList) {
    return new CamelDaysAvailabilityResponseMessage(dayAvailabilityList);
  }

  public static record DayAvailability(String day, boolean isAvailable) {
    public static DayAvailability of(String day, boolean isAvailable) {
      return new DayAvailability(day, isAvailable);
    }
  }
}
