package com.hrs.core.repository.reservation;

import com.hrs.core.domain.reservation.HotelReservation;
import com.hrs.core.repository.shared.BaseRepository;
import java.time.LocalDate;
import java.util.List;

public interface HotelReservationRepository extends BaseRepository<HotelReservation, Long> {
  boolean isConflictReservation(
      Long roomId, LocalDate checkInDate, LocalDate checkOutDate, Long excludeReservationId);

  Long getHotelIdByReservationId(Long reservationId);

  boolean authorizeUser(Long reservationId, Long userId);

  List<HotelReservation> findReservationsByUserId(Long userId);
}
