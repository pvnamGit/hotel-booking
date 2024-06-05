package com.hrs.core.service.hotel.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelUpdateRequest {
  private String name;
  private String address;
  private String city;
  private String country;
  private Integer noOfAvailableRooms;
}
