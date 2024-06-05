package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.request.HotelCreateRequest;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateHotelUseCase {
    private final HotelService hotelService;
    public HotelDetailResponse create(HotelCreateRequest request) {
        return hotelService.createHotel(request);
    }
}
