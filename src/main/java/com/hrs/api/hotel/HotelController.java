package com.hrs.api.hotel;

import com.hrs.api.shared.BaseEntityResponse;
import com.hrs.api.shared.BasePaginationResponse;
import com.hrs.application.usecase.hotel.*;
import com.hrs.core.service.hotel.request.*;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
import com.hrs.core.service.hotel.response.HotelRoomDetailResponse;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.prefix}/hotels")
@AllArgsConstructor
@CacheConfig(cacheNames = {"hotels", "hotel_rooms"})
public class HotelController {

  private final FindHotelsUseCase findHotelsUseCase;
  private final GetHotelDetailUseCase getHotelDetailUseCase;
  private final FindHotelRoomsUseCase findHotelRoomsUseCase;
  private final GetHotelRoomDetailUseCase getHotelRoomDetailUseCase;

  @GetMapping()
  @Cacheable(
      value = "hotels",
      key = "#searchCriteria?.location ?: 'default'",
      unless = "#searchCriteria.checkInDate != null || #searchCriteria.checkOutDate != null")
  public BasePaginationResponse findHotels(
      @ModelAttribute @Valid HotelSearchCriteria searchCriteria) {
    List<HotelDetailResponse> hotels = findHotelsUseCase.findHotelsByCriteria(searchCriteria);
    return BasePaginationResponse.success(hotels);
  }

  @GetMapping("/{id}")
  @Cacheable(value = "hotels", key = "#id")
  public BaseEntityResponse getHotelDetail(@PathVariable(name = "id") Long id) {
    HotelDetailResponse hotel = getHotelDetailUseCase.getHotel(id);
    return BaseEntityResponse.success(hotel);
  }

  // ============= Hotel Room ================

  @GetMapping("/{id}/rooms")
  @Cacheable(value = "hotel_rooms", key = "#hotelId ?: 'default'")
  public BasePaginationResponse findHotelRooms(@PathVariable(name = "id") Long hotelId) {
    List<HotelRoomDetailResponse> hotelRooms = findHotelRoomsUseCase.find(hotelId);
    return BasePaginationResponse.success(hotelRooms);
  }

  @GetMapping("/{id}/rooms/{roomId}")
  @Cacheable(value = "hotel_rooms", key = "#roomId ?: 'default'")
  public BaseEntityResponse getHotelRoomDetail(
      @PathVariable(name = "id") Long hotelId, @PathVariable(name = "roomId") Long roomId) {
    HotelRoomDetailResponse hotelRoom = getHotelRoomDetailUseCase.getRoom(hotelId, roomId);
    return BaseEntityResponse.success(hotelRoom);
  }
}
