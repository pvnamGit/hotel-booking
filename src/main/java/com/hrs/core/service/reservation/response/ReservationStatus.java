package com.hrs.core.service.reservation.response;

import reactor.util.annotation.Nullable;

import java.time.LocalDate;

public enum ReservationStatus {
  UPCOMING,
  ON_GOING,
  DONE,
  CANCELLED;

  public static ReservationStatus getStatus(
      LocalDate checkInDate, LocalDate checkOutDate, @Nullable Long cancelledAt) {
    if (cancelledAt != null) return CANCELLED;
    LocalDate currentDate = LocalDate.now();
    if (checkInDate.isAfter(currentDate)) {
      return UPCOMING;
    } else if (checkInDate.isBefore(currentDate) && checkOutDate.isAfter(currentDate)) {
      return ON_GOING;
    }
    return DONE;
  }
}
