package com.hrs.application.dto.reservation.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class HotelReservationUpdateRequest {
    private Long hotelRoomId;
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$", message = "Invalid check-in date format. Expected format: DD-MM-YYYY")
    private String checkInDate;
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$", message = "Invalid check-in date format. Expected format: DD-MM-YYYY")
    private String checkOutDate;

    @Min(1)
    private Integer noOfGuests;
}
