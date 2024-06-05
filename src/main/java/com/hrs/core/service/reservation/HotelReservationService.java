package com.hrs.core.service.reservation;

import com.hrs.core.service.reservation.request.HotelReservationCreateRequest;
import com.hrs.core.service.reservation.request.HotelReservationUpdateRequest;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;

import java.util.List;

public interface HotelReservationService {
    HotelReservationDetailResponse createReservation(HotelReservationCreateRequest createReservation);
    HotelReservationDetailResponse updateReservation(Long id, HotelReservationUpdateRequest createReservation);
    List<HotelReservationDetailResponse> getReservations();
    HotelReservationDetailResponse getReservationDetail(Long id);
    void cancelReservation(Long id);
}
