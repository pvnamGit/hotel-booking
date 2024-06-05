package com.hrs.application.usecase.reservation;

import com.hrs.core.service.reservation.HotelReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CancelReservationUseCase {
  private final HotelReservationService hotelReservationService;

  public void delete(Long id) {
    hotelReservationService.cancelReservation(id);
  }
}
