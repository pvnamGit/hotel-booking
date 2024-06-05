package com.hrs.application.usecase.reservation;

import com.hrs.core.service.reservation.HotelReservationService;
import com.hrs.core.service.reservation.request.HotelReservationUpdateRequest;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateReservationUseCase {
  private final HotelReservationService hotelReservationService;

  public HotelReservationDetailResponse update(Long id, HotelReservationUpdateRequest request) {
    return hotelReservationService.updateReservation(id, request);
  }
}
