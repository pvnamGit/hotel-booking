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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.prefix}/hotels")
@AllArgsConstructor
public class HotelController {

  private final FindHotelsUseCase findHotelsUseCase;
  private final GetHotelDetailUseCase getHotelDetailUseCase;
  private final FindHotelRoomUseCase findHotelRoomUseCase;
  private final GetHotelRoomDetailUseCase getHotelRoomDetailUseCase;

  @GetMapping()
  public BasePaginationResponse findHotels(
      @ModelAttribute @Valid HotelSearchCriteria searchCriteria) {
    List<HotelDetailResponse> hotels = findHotelsUseCase.findHotelsByCriteria(searchCriteria);
    return BasePaginationResponse.success(hotels);
  }

  @GetMapping("/{id}")
  public BaseEntityResponse getHotelDetail(@PathVariable(name = "id") Long id) {
    HotelDetailResponse hotel = getHotelDetailUseCase.get(id);
    return BaseEntityResponse.success(hotel);
  }

  // ============= Hotel Room ================

  @GetMapping("/{id}/rooms")
  public BasePaginationResponse findHotelRooms(@PathVariable(name = "id") Long hotelId) {
    List<HotelRoomDetailResponse> hotelRooms = findHotelRoomUseCase.find(hotelId);
    return BasePaginationResponse.success(hotelRooms);
  }

  @GetMapping("/{id}/rooms/{roomId}")
  public BaseEntityResponse getHotelRoomDetail(
      @PathVariable(name = "id") Long hotelId, @PathVariable(name = "roomId") Long roomId) {
    HotelRoomDetailResponse hotelRoom = getHotelRoomDetailUseCase.get(hotelId, roomId);
    return BaseEntityResponse.success(hotelRoom);
  }
}
