package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.application.dto.hotel.request.HotelSearchCriteria;
import com.hrs.application.dto.hotel.response.HotelDetailResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FindHotelsUseCase {
  private final HotelService hotelService;

  public List<HotelDetailResponse> findHotelsByCriteria(HotelSearchCriteria searchCriteria) {
    return hotelService.findHotels(searchCriteria);
  }
}
