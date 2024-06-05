package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.request.HotelSearchCriteria;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FindHotelByLocationUseCase {
    private final HotelService hotelService;
    public List<HotelDetailResponse> findByLocation(HotelSearchCriteria searchCriteria) {
        return hotelService.findHotels(searchCriteria);
    }
}
