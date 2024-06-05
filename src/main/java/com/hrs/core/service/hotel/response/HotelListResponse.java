package com.hrs.core.service.hotel.response;

import com.hrs.core.domain.hotel.Hotel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Builder
@Getter
@Setter
public class HotelListResponse implements Serializable {
   private List<HotelDetailResponse> hotels;
}
