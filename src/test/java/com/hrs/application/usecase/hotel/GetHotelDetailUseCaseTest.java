package com.hrs.application.usecase.hotel;

import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.repository.hotel.HotelRepository;
import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetHotelDetailUseCaseTest {
  @InjectMocks GetHotelDetailUseCase useCase;
  @Mock private HotelRepository hotelRepository;
  @Mock private HotelService hotelService;
  private Hotel hotel;
  private final Long ID = 1L;

  @BeforeEach
  public void setup() {
    hotel = new Hotel();
    hotel.setId(ID);
    hotel.setName("Test Hotel");
    hotel.setCountry("Test Country");
    hotel.setAddress("123 Test St");
    hotel.setCity("Test City");
    hotel.setNoOfRooms(50);
  }

  @Test
  public void testGetHotelDetailShouldReturnHotelDetail() {
    Mockito.when(hotelRepository.findById(ID)).thenReturn(hotel);
    Mockito.when(hotelService.getHotelDetail(ID))
        .thenReturn(HotelDetailResponse.build(hotel));
    HotelDetailResponse response = useCase.get(ID);
    Assertions.assertEquals(response.getId(), ID);
    Assertions.assertEquals(response.getName(), "Test Hotel");
  }
}
