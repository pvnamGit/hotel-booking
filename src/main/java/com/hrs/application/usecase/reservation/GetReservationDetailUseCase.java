package com.hrs.application.usecase.reservation;

import com.hrs.core.service.reservation.HotelReservationService;
import com.hrs.application.dto.reservation.response.HotelReservationDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
public class GetReservationDetailUseCase {
  private final HotelReservationService hotelReservationService;

  public HotelReservationDetailResponse getDetail(Long id) throws EntityNotFoundException {
    return hotelReservationService.getReservationDetail(id);
  }
}
