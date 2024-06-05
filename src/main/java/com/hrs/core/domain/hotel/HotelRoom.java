package com.hrs.core.domain.hotel;

import com.hrs.core.domain.reservation.HotelReservation;
import com.hrs.core.domain.shared.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "hotel_rooms")
@Where(clause = "is_active = true")
public class HotelRoom extends BaseEntity {
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "no_of_bedrooms", nullable = false)
    private Integer noOfBedrooms;
    @Column(name = "no_of_beds", nullable = false)
    private Integer noOfBeds;
    @Column(name = "no_of_bathrooms", nullable = false)
    private Integer noOfBathrooms;
    @Column(name = "price")
    private Double price;
    @Column(name = "no_of_guests")
    private Integer noOfGuests;


    @ManyToOne(targetEntity = Hotel.class)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "hotelRoom")
    private List<HotelReservation> hotelReservations;
}
