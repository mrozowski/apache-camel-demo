# reservation-demo
Simple reservation system for hotel. 
![Hexagonal architecture - Camel project](https://github.com/mrozowski/reservation-demo/assets/67066372/afab1a2a-bcda-4434-958b-792729d2d370)

## API
It has two rest endpoints. For making a reservation and checking the list of available dates for a given room.

## Apache camel
I made this demo to practice the Apache Camel framework for integrating systems, in this case, two modules responsible for two different things: Processing requests from users, and processing CSV files where data is stored.

## File
Data is stored in csv files. Each room has separate file. The csv file has given columns:

|  id |  name | surname  |  checkInDate | checkOutDate  | status |
|---|---|---|---|---|---|
| 381b5119-18d6-4fdd-acf7-592a85b08143  | Jarek  | Kowalski  | 2023-06-06  | 2023-06-08 | CONFIRMED  |

