package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.request.HotelRoomUpdateRequest;
import com.hrs.core.service.hotel.response.HotelRoomDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetHotelRoomDetailUseCase {
  private final HotelService hotelService;

  public HotelRoomDetailResponse get(Long hotelId, Long roomId) {
    return hotelService.getRoomDetail(hotelId, roomId);
  }
}
