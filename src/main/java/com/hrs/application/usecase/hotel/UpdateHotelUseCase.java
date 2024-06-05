package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.request.HotelUpdateRequest;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateHotelUseCase {
  private final HotelService hotelService;

  public HotelDetailResponse update(Long id, HotelUpdateRequest request) {
    return hotelService.updateHotel(id, request);
  }
}
