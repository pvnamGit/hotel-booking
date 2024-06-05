package com.hrs.application.usecase.reservation;

import com.hrs.core.service.reservation.HotelReservationService;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetReservationDetailUseCase {
  private final HotelReservationService hotelReservationService;

  public HotelReservationDetailResponse getDetail(Long id) {
    return hotelReservationService.getReservationDetail(id);
  }
}
