package com.hrs.application.usecase.reservation;

import static org.junit.jupiter.api.Assertions.*;

import com.hrs.application.dto.reservation.request.HotelReservationCreateRequest;
import com.hrs.application.dto.reservation.response.HotelReservationDetailResponse;
import com.hrs.core.repository.reservation.HotelReservationRepository;
import com.hrs.shared.enums.DateFormat;
import java.time.LocalDate;
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
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class CreateReservationUseCaseTest {
  LocalDate checkInDate, checkOutDate;
  String checkInDateRequest, checkOutDateRequest;
  @Autowired private CreateReservationUseCase createReservationUseCase;
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private HotelReservationRepository hotelReservationRepository;
  private HotelReservationCreateRequest request;

  @AfterEach
  public void cleanUp() {
    hotelReservationRepository.deleteAll();
  }

  @BeforeEach
  public void setUp() {
    checkInDate = LocalDate.now().plusDays(1).plusMonths(1);
    checkOutDate = LocalDate.now().plusDays(10).plusMonths(1);
    checkInDateRequest = checkInDate.format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter());
    checkOutDateRequest  = checkOutDate.format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter());
    request =
        HotelReservationCreateRequest.builder()
            .hotelId(1L)
            .hotelRoomId(1L)
            .checkInDate(checkInDateRequest)
            .checkOutDate(checkOutDateRequest)
            .noOfGuests(2)
            .build();
    var auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken("account@gmail.com", "password"));
    SecurityContextHolder.getContext()
        .setAuthentication(auth); // Set authentication in security context
  }

  @Test
  public void testCreateReservationSuccess() throws BadRequestException {

    HotelReservationDetailResponse result = createReservationUseCase.create(request);
    assertNotNull(result);
    assertEquals(checkInDate, result.getCheckInDate());
    assertEquals(checkOutDate, result.getCheckOutDate());
    assertEquals(2, result.getNoOfGuests());
  }

  @Test
  public void testCreateReservationDateConflict() throws BadRequestException {
    // Create an initial reservation to create a conflict
    HotelReservationCreateRequest initialRequest =
        HotelReservationCreateRequest.builder()
            .hotelId(1L)
            .hotelRoomId(1L)
            .checkInDate(checkInDateRequest)
            .checkOutDate(checkInDateRequest)
            .noOfGuests(2)
            .build();
    createReservationUseCase.create(initialRequest);

    BadRequestException exception =
        assertThrows(
            BadRequestException.class,
            () -> {
              createReservationUseCase.create(request);
            });

    assertEquals("Reservation dates conflict with existing reservations.", exception.getMessage());
  }

  @Test
  public void testCreateReservationRoomNotBelongToHotel() {
    // Change the hotelRoomId to a non-existing room
    request.setHotelRoomId(999L);

    BadRequestException exception =
        assertThrows(
            BadRequestException.class,
            () -> {
              createReservationUseCase.create(request);
            });

    assertEquals("Room does not belong to the hotel", exception.getMessage());
  }

  @Test
  public void testCreateReservationInvalidCheckInBeforeToday() {
    // Set the check-in date to a past date
    request.setCheckInDate(LocalDate.now().minusDays(1)
            .format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));

    BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> {
              createReservationUseCase.create(request);
            }
    );

    assertEquals("Check in date must be larger or equal to the current date", exception.getMessage());
  }

  @Test
  public void testCreateReservationCheckInAfterCheckOut() {
    // Set the check-in date to a date after the check-out date
    request.setCheckInDate(LocalDate.now().plusDays(5)
            .format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));
    request.setCheckOutDate(LocalDate.now().plusDays(3)
            .format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));

    BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> {
              createReservationUseCase.create(request);
            }
    );

    assertEquals("Check in date must be before check out date", exception.getMessage());
  }

  @Test
  public void testCreateReservationWithSameCheckInAndCheckOut() {
    // Set the check-in date equal to the check-out date
    request.setCheckInDate(LocalDate.now().plusDays(3)
            .format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));
    request.setCheckOutDate(LocalDate.now().plusDays(3)
            .format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));

    // Assuming same-day check-in and check-out is allowed; otherwise, update exception message accordingly
    assertDoesNotThrow(() -> createReservationUseCase.create(request));
  }

  @Test
  public void testCreateReservationWithValidFutureDates() {
    // Set the check-in and check-out dates to valid future dates
    request.setCheckInDate(LocalDate.now().plusDays(10)
            .format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));
    request.setCheckOutDate(LocalDate.now().plusDays(15)
            .format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));

    assertDoesNotThrow(() -> createReservationUseCase.create(request));
  }
}
