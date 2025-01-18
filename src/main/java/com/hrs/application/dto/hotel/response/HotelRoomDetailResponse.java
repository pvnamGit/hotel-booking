package com.hrs.application.dto.hotel.response;

import com.hrs.core.domain.hotel.HotelRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class HotelRoomDetailResponse implements Serializable {
  private Long id;
  private String code;
  private Integer noOfBedrooms;
  private Integer noOfBeds;
  private Integer noOfBathrooms;
  private Double price;
  private Integer noOfGuest;
  private Long hotelId;

  public static HotelRoomDetailResponse build(HotelRoom room) {
    return HotelRoomDetailResponse.builder()
        .id(room.getId())
        .code(room.getCode())
        .noOfBedrooms(room.getNoOfBedrooms())
        .noOfBeds(room.getNoOfBeds())
        .noOfBathrooms(room.getNoOfBathrooms())
        .price(room.getPrice() == null ? 0 : room.getPrice())
        .noOfGuest(room.getNoOfGuests() == null ? 0 : room.getNoOfGuests())
        .hotelId(room.getHotel().getId())
        .build();
  }
}
