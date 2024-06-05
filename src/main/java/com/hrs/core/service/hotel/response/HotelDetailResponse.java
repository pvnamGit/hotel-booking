package com.hrs.core.service.hotel.response;

import com.hrs.core.domain.hotel.Hotel;
import java.util.Collections;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import reactor.util.annotation.Nullable;

@Getter
@Setter
@Builder
public class HotelDetailResponse {
  private Long id;
  private String name;
  private String address;
  private String city;
  private String country;
  private Integer noOfRooms;

  public static HotelDetailResponse build(@Nullable Hotel hotel) {
    return HotelDetailResponse.builder()
        .id(hotel.getId())
        .name(hotel.getName())
        .address(hotel.getAddress())
        .city(hotel.getCity())
        .country(hotel.getCountry())
        .noOfRooms(hotel.getNoOfRooms())
        .build();
  }
}
