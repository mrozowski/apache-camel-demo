package com.mrozowski.demo.apachecamel.application.domain;

public record Day(String date, boolean isAvailable) {

  public static Day available(String day) {
    return new Day(day, true);
  }

  public static Day occupied(String day) {
    return new Day(day, false);
  }

  public static Day of(String day, boolean isAvailable) {
    return new Day(day, isAvailable);
  }
}
