package com.hrs.application.usecase.reservation;

import com.hrs.core.service.reservation.HotelReservationService;
import com.hrs.core.service.reservation.request.HotelReservationUpdateRequest;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateReservationUseCase {
  private final HotelReservationService hotelReservationService;

  public HotelReservationDetailResponse update(Long id, HotelReservationUpdateRequest request) throws BadRequestException {
    return hotelReservationService.updateReservation(id, request);
  }
}
