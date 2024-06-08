package com.hrs.application.usecase.hotel;

import com.hrs.core.service.hotel.request.HotelSearchCriteria;

import com.hrs.core.service.hotel.response.HotelDetailResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class FindHotelsUseCaseTest {
    @Autowired
    private FindHotelsUseCase findHotelsUseCase;

    @Test
    public void testFindHotelsByLocationShouldReturnListOfTestHotel(){
        HotelSearchCriteria searchCriteria =
                HotelSearchCriteria.builder().location("Test City").build();
        List<HotelDetailResponse> hotels = findHotelsUseCase.findHotelsByCriteria(searchCriteria);
        assertNotNull(hotels);
        assertEquals("Test City", hotels.get(0).getCity());
    }

    @Test
    public void testFindHotelsByNonExistLocationShouldReturnEmptyListOfTestHotel(){
        HotelSearchCriteria searchCriteria =
                HotelSearchCriteria.builder().location("Non-exist location").build();
        List<HotelDetailResponse> hotels = findHotelsUseCase.findHotelsByCriteria(searchCriteria);
        assertEquals(0, hotels.size());
    }
}
