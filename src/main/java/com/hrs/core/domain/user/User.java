package com.hrs.core.domain.user;

import com.hrs.core.domain.account.Account;
import com.hrs.core.domain.reservation.HotelReservation;
import com.hrs.core.domain.shared.BaseEntity;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
@Where(clause = "is_active = true")
public class User extends BaseEntity {
  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "year_of_birth")
  private String yearOfBirth;

  @Column(name = "phone")
  private String phone;

  @Column(name = "country_code")
  private String countryCode;

  @Column(name = "city_code")
  private String cityCode;

  @Column(name = "zip_code")
  private String zipCode;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id")
  private Account account;

  @OneToMany(mappedBy = "user")
  private List<HotelReservation> hotelReservations;
}
