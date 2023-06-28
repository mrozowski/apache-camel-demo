package com.mrozowski.demo.apachecamel.application.adapter.incoming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class ReservationRequest {

  private String name;
  private String surname;
  private String room;
  private String date;
  private int days;
}
