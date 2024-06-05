package com.hrs.core.service.hotel.request;

import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class HotelRoomUpdateRequest {
  @Length(min = 4, max = 10)
  private String code;

  @Min(1)
  @Nullable
  private Integer noOfBedrooms;

  @Min(1)
  @Nullable
  private Integer noOfBeds;

  @Min(1)
  @Nullable
  private Integer noOfBathrooms;
}
