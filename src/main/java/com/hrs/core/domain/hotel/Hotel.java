package com.hrs.core.domain.hotel;

import com.hrs.core.domain.reservation.HotelReservation;
import com.hrs.core.domain.shared.BaseEntity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Where;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "hotels")
@Where(clause = "is_active = true")
public class Hotel extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "no_of_rooms")
    private Integer noOfRooms = 0;
    @Column(name = "no_of_available_rooms")
    private Integer noOfAvailableRooms = 0;

    @OneToMany(mappedBy = "hotel")
    private List<HotelRoom> hotelRooms;

    @OneToMany(mappedBy = "hotel")
    private List<HotelReservation> hotelReservations;

    public void increaseRoom() {
        var currentRooms = this.noOfRooms == null ? 0 : this.noOfRooms;
        this.noOfRooms = currentRooms + 1;
    }

    public void decreaseRoom() {
        var currentRooms = this.noOfRooms == null ? 0 : this.noOfRooms;
        this.noOfRooms = currentRooms - 1;;
    }

    public void increaseAvailableRoom() {
        var currentRooms = this.noOfAvailableRooms == null ? 0 : this.noOfAvailableRooms;
        this.noOfAvailableRooms = currentRooms + 1;;
    }

    public void decreaseAvailableRoom() {
        var currentRooms = this.noOfAvailableRooms == null ? 0 : this.noOfAvailableRooms;
        this.noOfAvailableRooms = currentRooms - 1;
    }
}
