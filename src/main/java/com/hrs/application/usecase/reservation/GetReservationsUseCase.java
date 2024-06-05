package com.hrs.application.usecase.reservation;

import com.hrs.core.service.reservation.HotelReservationService;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetReservationsUseCase {
    private final HotelReservationService hotelReservationService;
    public List<HotelReservationDetailResponse> getReservations() {
        return hotelReservationService.getReservations();
    }
}
