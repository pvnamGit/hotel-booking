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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.prefix}/hotels")
@AllArgsConstructor
public class HotelController {

  private final CreateHotelUseCase createHotelUseCase;
  private final UpdateHotelUseCase updateHotelUseCase;
  private final FindHotelByLocationUseCase findHotelByLocationUseCase;
  private final GetHotelDetailUseCase getHotelDetailUseCase;
  private final DeleteHotelUseCase deleteHotelUseCase;
  private final CreateHotelRoomUseCase createHotelRoomUseCase;
  private final UpdateHotelRoomUseCase updateHotelRoomUseCase;
  private final FindHotelRoomUseCase findHotelRoomUseCase;
  private final GetHotelRoomDetailUseCase getHotelRoomDetailUseCase;
  private final DeleteHotelRoomUseCase deleteHotelRoomUseCase;

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public BaseEntityResponse createHotel(@Valid @RequestBody HotelCreateRequest request) {
    HotelDetailResponse hotelDetail = createHotelUseCase.create(request);
    return BaseEntityResponse.success(hotelDetail);
  }

  @PatchMapping("/{id}")
  public BaseEntityResponse updateHotel(
      @PathVariable(name = "id") Long id, @Valid @RequestBody HotelUpdateRequest request) {
    HotelDetailResponse hotelDetail = updateHotelUseCase.update(id, request);
    return BaseEntityResponse.success(hotelDetail);
  }

  @GetMapping()
  public BasePaginationResponse findHotels(@ModelAttribute @Valid HotelSearchCriteria searchCriteria) {
    List<HotelDetailResponse> hotels = findHotelByLocationUseCase.findByLocation(searchCriteria);
    return BasePaginationResponse.success(hotels);
  }

  @GetMapping("/{id}")
  public BaseEntityResponse getHotelDetail(@PathVariable(name = "id") Long id) {
    HotelDetailResponse hotel = getHotelDetailUseCase.get(id);
    return BaseEntityResponse.success(hotel);
  }

  @DeleteMapping("/{id}")
  public BaseEntityResponse deleteHotel(@PathVariable(name = "id") Long id) {
    deleteHotelUseCase.delete(id);
    return BaseEntityResponse.success();
  }

  // ============= Hotel Room ================

  @PostMapping("/{id}/rooms")
  @ResponseStatus(HttpStatus.CREATED)
  public BaseEntityResponse createHotelRoom(
      @PathVariable(name = "id") Long hotelId, @Valid @RequestBody HotelRoomCreateRequest request) {
    HotelRoomDetailResponse hotelRoomDetail = createHotelRoomUseCase.create(hotelId, request);
    return BaseEntityResponse.success(hotelRoomDetail);
  }

  @PatchMapping("{id}/rooms/{roomId}")
  public BaseEntityResponse updateHotelRoom(
      @PathVariable(name = "id") Long hotelId,
      @PathVariable(name = "roomId") Long roomId,
      @Valid @RequestBody HotelRoomUpdateRequest request) {
    HotelRoomDetailResponse hotelRoomDetail =
        updateHotelRoomUseCase.update(hotelId, roomId, request);
    return BaseEntityResponse.success(hotelRoomDetail);
  }

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

  @DeleteMapping("/{id}/rooms/{roomId}")
  public BaseEntityResponse deleteHotelRoom(
      @PathVariable(name = "id") Long hotelId, @PathVariable(name = "roomId") Long roomId) {
    deleteHotelRoomUseCase.delete(hotelId, roomId);
    return BaseEntityResponse.success();
  }
}
