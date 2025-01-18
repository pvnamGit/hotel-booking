package com.hrs.application.helper.reservation;

import com.hrs.core.exception.reservation.HotelReservationErrorMessages;
import java.time.LocalDate;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class ReservationValidator {
  public void validateCheckInAndCheckOut(LocalDate checkInDate, LocalDate checkOutDate)
      throws BadRequestException {
    if (checkInDate.isBefore(LocalDate.now()))
      throw new BadRequestException(HotelReservationErrorMessages.CHECK_IN_BEFORE_NOW.getMessage());
    if (checkInDate.isAfter(checkOutDate))
      throw new BadRequestException(
          HotelReservationErrorMessages.CHECK_IN_AFTER_CHECK_OUT.getMessage());
  }

  public void validateConflict(boolean isConflict) throws BadRequestException {
    if (isConflict)
      throw new BadRequestException(
          HotelReservationErrorMessages.RESERVATION_CONFLICT.getMessage());
  }

  public void validateHotelRoom(boolean isValid) throws BadRequestException {
    if (!isValid)
      throw new BadRequestException(
          HotelReservationErrorMessages.ROOM_NOT_BELONG_TO_HOTEL.getMessage());
  }
}
