package com.hrs.application.usecase.hotel;

import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.repository.hotel.HotelRepository;
import com.hrs.core.service.hotel.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class GetHotelDetailUseCaseTest {
    @InjectMocks
    GetHotelDetailUseCase useCase;
    @Mock
    private HotelRepository hotelRepository;
    @Mock private HotelService hotelService;
    private Hotel hotel;
    @BeforeEach
    public void setup() {
        hotel = Hotel.builder()
                .name("Test Hotel")
                .country("Test Country")
                .address("123 Test St")
                .city("Test City")
                .noOfAvailableRooms(50)
                .build();
    }
}
