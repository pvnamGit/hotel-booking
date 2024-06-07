package com.hrs.application.usecase.reservation;

import com.hrs.core.service.reservation.HotelReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
public class CancelReservationUseCase {
  private final HotelReservationService hotelReservationService;

  public void cancel(Long id) throws EntityNotFoundException {
    hotelReservationService.cancelReservation(id);
  }
}
