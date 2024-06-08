package com.hrs.application.usecase.reservation;

import com.hrs.core.service.reservation.HotelReservationService;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
public class CancelReservationUseCase {
  private final HotelReservationService hotelReservationService;

  public HotelReservationDetailResponse cancel(Long id) throws EntityNotFoundException {
    return hotelReservationService.cancelReservation(id);
  }
}
