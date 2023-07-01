package com.mrozowski.demo.apachecamel.application.adapter.incoming

class Fixture {

  def static NAME = "someName"
  def static SURNAME = "someSurname"
  def static ROOM_ID = "1"
  def static RESERVATION_DATE_IN = "2023-05-20"
  def static RESERVATION_DATE_OUT = "2023-05-22"
  def static NUMBER_OF_DAYS = 2
  def static RESERVATION_REQUEST = new ReservationRequest(
      name: NAME,
      surname: SURNAME,
      room: ROOM_ID,
      date: RESERVATION_DATE_IN,
      days: NUMBER_OF_DAYS)

}
