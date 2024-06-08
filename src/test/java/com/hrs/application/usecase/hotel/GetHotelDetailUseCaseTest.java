package com.hrs.application.usecase.hotel;

import static org.junit.jupiter.api.Assertions.*;

import com.hrs.core.service.hotel.request.HotelSearchCriteria;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class GetHotelDetailUseCaseTest {
  @Autowired GetHotelDetailUseCase getHotelDetailUseCase;

  @Autowired FindHotelsUseCase findHotelsUseCase;

  @Test
  public void testGetHotelDetailShouldReturnHotelDetailNotNull() {
    HotelSearchCriteria searchCriteria = HotelSearchCriteria.builder().build();
    List<HotelDetailResponse> hotels = findHotelsUseCase.findHotelsByCriteria(searchCriteria);
    assertTrue(hotels.size() > 0);
    Long hotelId = hotels.get(0).getId();
    HotelDetailResponse hotelDetail = getHotelDetailUseCase.getHotel(hotelId);
    assertNotNull(hotelDetail);
  }

  @Test
  public void testGetHotelDetailShouldThrowEntityNotFound() {
    assertThrows(EntityNotFoundException.class, () -> getHotelDetailUseCase.getHotel(0L));
  }
}
