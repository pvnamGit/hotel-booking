package com.hrs.core.service.reservation;

import com.hrs.core.service.reservation.request.HotelReservationCreateRequest;
import com.hrs.core.service.reservation.request.HotelReservationUpdateRequest;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public interface HotelReservationService {
  HotelReservationDetailResponse createReservation(HotelReservationCreateRequest createReservation)
      throws BadRequestException;

  HotelReservationDetailResponse updateReservation(
      Long id, HotelReservationUpdateRequest createReservation)
      throws BadRequestException;

  List<HotelReservationDetailResponse> getReservations();

  HotelReservationDetailResponse getReservationDetail(Long id) throws EntityNotFoundException;

  HotelReservationDetailResponse cancelReservation(Long id) throws EntityNotFoundException;
}
