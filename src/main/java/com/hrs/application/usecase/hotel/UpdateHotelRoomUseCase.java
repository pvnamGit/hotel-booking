package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.request.HotelRoomUpdateRequest;
import com.hrs.core.service.hotel.response.HotelRoomDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateHotelRoomUseCase {
  private final HotelService hotelService;

  public HotelRoomDetailResponse update(Long hotelId, Long roomId, HotelRoomUpdateRequest request) {
    return hotelService.updateRoom(hotelId, roomId, request);
  }
}
