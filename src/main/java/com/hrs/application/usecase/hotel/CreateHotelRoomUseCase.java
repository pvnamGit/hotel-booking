package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.request.HotelRoomCreateRequest;
import com.hrs.core.service.hotel.response.HotelRoomDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CreateHotelRoomUseCase {
    private final HotelService hotelService;
    public HotelRoomDetailResponse create(Long hotelId, HotelRoomCreateRequest request) {
        return hotelService.createRoom(hotelId, request);
    }
}
