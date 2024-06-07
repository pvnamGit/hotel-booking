package com.hrs.application.usecase.reservation;

import com.hrs.core.service.reservation.HotelReservationService;
import com.hrs.core.service.reservation.request.HotelReservationCreateRequest;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateReservationUseCase {
  private final HotelReservationService hotelReservationService;

  public HotelReservationDetailResponse create(HotelReservationCreateRequest request) throws BadRequestException {
    return hotelReservationService.createReservation(request);
  }
}
