package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.application.dto.hotel.response.HotelDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetHotelDetailUseCase {
  private final HotelService hotelService;

  public HotelDetailResponse getHotel(Long id) {
    return hotelService.getHotelDetail(id);
  }
}
