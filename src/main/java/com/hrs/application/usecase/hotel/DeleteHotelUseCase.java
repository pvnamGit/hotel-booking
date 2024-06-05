package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.request.HotelCreateRequest;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteHotelUseCase {
  private final HotelService hotelService;

  public void delete(Long id) {
    hotelService.deleteHotel(id);
  }
}
