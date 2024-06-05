package com.hrs.core.service.hotel.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class HotelRoomCreateRequest {
  @NotBlank(message = "Room code is mandatory")
  @Length(min = 4, max = 10)
  private String code;

  @NotNull(message = "Number of bedrooms is mandatory")
  @Min(1)
  private Integer noOfBedrooms;

  @NotNull(message = "Number of bedrooms is mandatory")
  @Min(1)
  private Integer noOfBeds;

  @NotNull(message = "Number of bedrooms is mandatory")
  @Min(1)
  private Integer noOfBathrooms;
}
