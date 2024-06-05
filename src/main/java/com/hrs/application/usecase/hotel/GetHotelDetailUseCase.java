package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetHotelDetailUseCase {
    private final HotelService hotelService;
    public HotelDetailResponse get(Long id) {
        return hotelService.getHotelDetail(id);
    }
}
