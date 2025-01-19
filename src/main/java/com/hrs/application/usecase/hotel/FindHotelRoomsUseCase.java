package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.application.dto.hotel.response.HotelRoomDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FindHotelRoomsUseCase {
  private final HotelService hotelService;

  public List<HotelRoomDetailResponse> find(Long hotelId) {
    return hotelService.findRooms(hotelId);
  }
}
