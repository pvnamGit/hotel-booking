package com.hrs.application.usecase.hotel;

import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.repository.hotel.HotelRepository;
import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.request.HotelSearchCriteria;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ActiveProfiles(profiles = "test")
public class FindHotelsUseCaseTest {
  @InjectMocks FindHotelsUseCase useCase;
  @Mock private HotelRepository hotelRepository;
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

  @Test
  public void testFindHotelsByLocationShouldReturnNotEmptyResult() {
    HotelSearchCriteria searchCriteria =
        HotelSearchCriteria.builder().location("Test City").build();
    Mockito.when(hotelRepository.findHotels(Mockito.any())).thenReturn(Arrays.asList(hotel));
    HotelDetailResponse hotelDetailResponse = HotelDetailResponse.build(hotel);
    Mockito.when(hotelService.findHotels(Mockito.any()))
        .thenReturn(Arrays.asList(hotelDetailResponse));
    List<HotelDetailResponse> responses = useCase.findHotelsByCriteria(searchCriteria);
    Assertions.assertNotNull(responses);
    Assertions.assertEquals(1, responses.size());
    Assertions.assertEquals("Test Hotel", responses.get(0).getName());
  }

  @Test
  public void testFindHotelsByLocationShouldReturnEmptyResult() {
    HotelSearchCriteria searchCriteria =
        HotelSearchCriteria.builder().location("Non-exist city").build();
    Mockito.when(hotelRepository.findHotels(Mockito.any())).thenReturn(Collections.emptyList());
    Mockito.when(hotelService.findHotels(Mockito.any())).thenReturn(Collections.emptyList());
    List<HotelDetailResponse> responses = useCase.findHotelsByCriteria(searchCriteria);
    Assertions.assertEquals(0, responses.size());
  }
}
