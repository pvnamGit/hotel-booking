package com.hrs.api.reservation;

import com.hrs.api.shared.BaseEntityResponse;
import com.hrs.api.shared.BasePaginationResponse;
import com.hrs.application.usecase.reservation.*;
import com.hrs.core.service.reservation.request.HotelReservationCreateRequest;
import com.hrs.core.service.reservation.request.HotelReservationUpdateRequest;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.prefix}/reservations")
@AllArgsConstructor
public class HotelReservationController {

  private final CreateReservationUseCase createReservationUseCase;
  private final UpdateReservationUseCase updateReservationUseCase;
  private final GetReservationsUseCase getReservationsUseCase;
  private final GetReservationDetailUseCase getReservationDetailUseCase;
  private final CancelReservationUseCase cancelReservationUseCase;

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public BaseEntityResponse createReservation(
      @Valid @RequestBody HotelReservationCreateRequest request) throws BadRequestException {
    HotelReservationDetailResponse hotelDetail = createReservationUseCase.create(request);
    return BaseEntityResponse.success(hotelDetail);
  }

  @PatchMapping("/{id}")
  public BaseEntityResponse updateReservation(
      @PathVariable(name = "id") Long id, @Valid @RequestBody HotelReservationUpdateRequest request)
      throws BadRequestException {
    HotelReservationDetailResponse hotelDetail = updateReservationUseCase.update(id, request);
    return BaseEntityResponse.success(hotelDetail);
  }

  @GetMapping()
  public BasePaginationResponse getReservations() {
    List<HotelReservationDetailResponse> hotels = getReservationsUseCase.getReservations();
    return BasePaginationResponse.success(hotels);
  }

  @GetMapping("/{id}")
  public BaseEntityResponse getReservationDetail(@PathVariable(name = "id") Long id) {
    HotelReservationDetailResponse hotel = getReservationDetailUseCase.getDetail(id);
    return BaseEntityResponse.success(hotel);
  }

  @DeleteMapping("/{id}")
  public BaseEntityResponse deleteReservation(@PathVariable(name = "id") Long id) {
    cancelReservationUseCase.cancel(id);
    return BaseEntityResponse.success();
  }
}