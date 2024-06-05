package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.request.HotelRoomUpdateRequest;
import com.hrs.core.service.hotel.response.HotelRoomDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteHotelRoomUseCase {
  private final HotelService hotelService;

  public void delete(Long hotelId, Long roomId) {
    hotelService.deleteRoom(hotelId, roomId);
  }
}
