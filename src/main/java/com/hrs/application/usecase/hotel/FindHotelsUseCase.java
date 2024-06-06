package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.request.HotelSearchCriteria;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class FindHotelsUseCase {
    private HotelService hotelService;
    public List<HotelDetailResponse> findHotelsByCriteria(HotelSearchCriteria searchCriteria) {
        return hotelService.findHotels(searchCriteria);
    }
}
