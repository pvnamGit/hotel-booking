package com.hrs.core.service.reservation.response;

import com.hrs.core.domain.reservation.HotelReservation;
import com.sun.istack.Nullable;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class HotelReservationDetailResponse implements Serializable {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer noOfGuests;
    private Double totalPrice;
    private ReservationStatus reservationStatus;
    @Nullable
    private Long cancelledAt;

    public static HotelReservationDetailResponse build(HotelReservation hotelReservation) {
        return HotelReservationDetailResponse.builder()
                .id(hotelReservation.getId())
                .checkInDate(hotelReservation.getCheckInDate())
                .checkOutDate(hotelReservation.getCheckOutDate())
                .noOfGuests(hotelReservation.getNoOfGuests())
                .totalPrice(hotelReservation.getTotalPrice())
                .reservationStatus(ReservationStatus.getStatus(
                        hotelReservation.getCheckInDate(),
                        hotelReservation.getCheckOutDate(),
                        hotelReservation.getCancelledAt())
                )
                .cancelledAt(hotelReservation.getCancelledAt())
                .build();
    }
}
