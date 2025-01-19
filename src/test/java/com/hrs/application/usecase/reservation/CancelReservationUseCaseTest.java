package com.hrs.application.usecase.reservation;

import static org.junit.jupiter.api.Assertions.*;

import com.hrs.core.repository.reservation.HotelReservationRepository;
import com.hrs.application.dto.reservation.request.HotelReservationCreateRequest;
import com.hrs.application.dto.reservation.response.HotelReservationDetailResponse;
import com.hrs.application.dto.reservation.response.ReservationStatus;
import com.hrs.shared.enums.DateFormat;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

@SpringBootTest
@ActiveProfiles("test")
public class CancelReservationUseCaseTest {

  private final Long ID = 1L;
  LocalDate checkInDate, checkOutDate;
  String checkInDateRequest, checkOutDateRequest;
  @Autowired private CancelReservationUseCase cancelReservationUseCase;
  @Autowired private CreateReservationUseCase createReservationUseCase;
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private HotelReservationRepository hotelReservationRepository;

  private HotelReservationDetailResponse reservation;

  @AfterEach
  public void cleanUp() {
    hotelReservationRepository.deleteAll();
  }

  @BeforeEach
  public void setUp() throws BadRequestException {
    checkInDate = LocalDate.now().plusDays(1).plusMonths(1);
    checkOutDate = LocalDate.now().plusDays(10).plusMonths(1);
    checkInDateRequest = checkInDate.format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter());
    checkOutDateRequest  = checkOutDate.plusMonths(1).format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter());
    HotelReservationCreateRequest request =
        HotelReservationCreateRequest.builder()
            .hotelId(ID)
            .hotelRoomId(ID)
            .checkInDate(checkInDateRequest)
            .checkOutDate(checkOutDateRequest)
            .noOfGuests(2)
            .build();
    var auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken("account@gmail.com", "password"));
    SecurityContextHolder.getContext()
        .setAuthentication(auth); // Set authentication in security context
    reservation = createReservationUseCase.create(request);
  }

  @Test
  public void testCancelReservationSuccess() {
    HotelReservationDetailResponse hotelReservation =
        cancelReservationUseCase.cancel(reservation.getId());
    assertEquals(hotelReservation.getReservationStatus(), ReservationStatus.CANCELLED);
    assertNotNull(hotelReservation.getCancelledAt());
  }
}
