package com.hrs.core.domain.reservation;

import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.core.domain.shared.BaseEntity;
import com.hrs.core.domain.user.User;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Where;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "hotel_reservations")
@Where(clause = "is_active = true")
public class HotelReservation extends BaseEntity {
  private LocalDate checkInDate;
  private LocalDate checkOutDate;
  private Integer noOfGuests;
  private String specialRequests;
  private Double totalPrice;
  private Long cancelledAt;

  @ManyToOne()
  @JoinColumn(name = "hotel_id")
  private Hotel hotel;

  @ManyToOne()
  @JoinColumn(name = "hotel_room_id")
  private HotelRoom hotelRoom;

  @ManyToOne()
  @JoinColumn(name = "user_id")
  private User user;

}
