package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.application.dto.hotel.response.HotelRoomDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetHotelRoomDetailUseCase {
  private final HotelService hotelService;

  public HotelRoomDetailResponse getRoom(Long hotelId, Long roomId) {
    return hotelService.getRoomDetail(hotelId, roomId);
  }
}
