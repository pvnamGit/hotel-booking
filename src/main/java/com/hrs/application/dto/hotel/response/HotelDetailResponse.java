package com.hrs.application.dto.hotel.response;

import com.hrs.core.domain.hotel.Hotel;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
public class HotelDetailResponse implements Serializable {
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
