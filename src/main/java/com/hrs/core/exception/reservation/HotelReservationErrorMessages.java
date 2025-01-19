package com.hrs.core.exception.reservation;

import lombok.Getter;

@Getter
public enum HotelReservationErrorMessages {
    CHECK_IN_BEFORE_NOW("Check in date must be larger or equal to the current date"),
    CHECK_IN_AFTER_CHECK_OUT("Check in date must be before check out date"),
    RESERVATION_CONFLICT("Reservation dates conflict with existing reservations."),
    ROOM_NOT_BELONG_TO_HOTEL("Room does not belong to the hotel"),
    UNAUTHORIZED("Unauthorized"),
    CAN_NOT_UPDATE_CANCELLED_RESERVATION("Can't update cancelled reservation");

    private final String message;

    HotelReservationErrorMessages(String message) {
        this.message = message;
    }

}
