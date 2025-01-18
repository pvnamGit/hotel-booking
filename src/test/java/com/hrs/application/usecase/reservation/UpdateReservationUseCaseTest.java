package com.hrs.application.usecase.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hrs.core.repository.reservation.HotelReservationRepository;
import com.hrs.application.dto.reservation.request.HotelReservationCreateRequest;
import com.hrs.application.dto.reservation.request.HotelReservationUpdateRequest;
import com.hrs.application.dto.reservation.response.HotelReservationDetailResponse;
import java.time.LocalDate;

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
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class UpdateReservationUseCaseTest {
  private final Long ID = 1L;
  LocalDate checkInDate, checkOutDate;
  String checkInDateRequest, checkOutDateRequest;
  @Autowired private UpdateReservationUseCase updateReservationUseCase;
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
  public void testUpdateReservationSuccess() throws BadRequestException {
    HotelReservationUpdateRequest updateRequest = new HotelReservationUpdateRequest();
    LocalDate newCheckInDate = LocalDate.now().plusDays(2).plusMonths(1);
    LocalDate newCheckOutDate = LocalDate.now().plusDays(11).plusMonths(1);
    updateRequest.setCheckInDate(newCheckInDate.format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));
    updateRequest.setCheckOutDate(newCheckOutDate.format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));
    updateRequest.setNoOfGuests(3);

    // Perform the test
    HotelReservationDetailResponse response =
        updateReservationUseCase.update(reservation.getId(), updateRequest);

    // Verify the result
    assertEquals(newCheckInDate, response.getCheckInDate());
    assertEquals(newCheckOutDate, response.getCheckOutDate());
    assertEquals(response.getNoOfGuests(), 3);
  }

  @Test
  public void testUpdateReservationDateConflict() throws BadRequestException {
    LocalDate newCheckInDate = LocalDate.now().plusMonths(2).plusMonths(1);
    LocalDate newCheckOutDate = LocalDate.now().plusMonths(3).plusMonths(1);
    var newReservation =
        createReservationUseCase.create(
            HotelReservationCreateRequest.builder()
                .hotelId(ID)
                .hotelRoomId(ID)
                .checkInDate(newCheckInDate.format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()))
                .checkOutDate(newCheckOutDate.format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()))
                .noOfGuests(2)
                .build());



    HotelReservationUpdateRequest updateRequest = new HotelReservationUpdateRequest();
    updateRequest.setCheckInDate(checkInDateRequest);
    updateRequest.setCheckOutDate(checkOutDateRequest);
    updateRequest.setNoOfGuests(3);
    updateRequest.setHotelRoomId(ID);
    // throw conflict reservation date with the reservation on setup
    BadRequestException exception =
        assertThrows(
            BadRequestException.class,
            () -> {
              updateReservationUseCase.update(newReservation.getId(), updateRequest);
            });

    assertEquals("Reservation dates conflict with existing reservations.", exception.getMessage());
  }

  @Test
  public void testUpdateReservationInvalidCheckInBeforeToday() {
    // Set the check-in date to a past date
    HotelReservationUpdateRequest updateRequest = new HotelReservationUpdateRequest();
    updateRequest.setCheckInDate(
            LocalDate.now().minusDays(1).format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));
    updateRequest.setCheckOutDate(checkOutDateRequest);
    updateRequest.setNoOfGuests(3);

    // Expect an exception
    BadRequestException exception =
            assertThrows(
                    BadRequestException.class,
                    () -> updateReservationUseCase.update(reservation.getId(), updateRequest));

    assertEquals("Check in date must be larger or equal to the current date", exception.getMessage());
  }

  @Test
  public void testUpdateReservationCheckInAfterCheckOut() {
    // Set the check-in date to a date after the check-out date
    HotelReservationUpdateRequest updateRequest = new HotelReservationUpdateRequest();
    updateRequest.setCheckInDate(
            checkOutDate.plusDays(1).format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));
    updateRequest.setCheckOutDate(
            checkOutDate.format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));
    updateRequest.setNoOfGuests(3);

    // Expect an exception
    BadRequestException exception =
            assertThrows(
                    BadRequestException.class,
                    () -> updateReservationUseCase.update(reservation.getId(), updateRequest));

    assertEquals("Check in date must be before check out date", exception.getMessage());
  }

  @Test
  public void testUpdateReservationRoomNotBelongToHotel() {
    // Set an invalid hotel room ID
    HotelReservationUpdateRequest updateRequest = new HotelReservationUpdateRequest();
    updateRequest.setCheckInDate(checkInDateRequest);
    updateRequest.setCheckOutDate(checkOutDateRequest);
    updateRequest.setNoOfGuests(3);
    updateRequest.setHotelRoomId(999L); // Invalid room ID

    // Expect an exception
    BadRequestException exception =
            assertThrows(
                    BadRequestException.class,
                    () -> updateReservationUseCase.update(reservation.getId(), updateRequest));

    assertEquals("Room does not belong to the hotel", exception.getMessage());
  }

  @Test
  public void testUpdateReservationWithNoChanges() throws BadRequestException {
    // Use the same values as the current reservation
    HotelReservationUpdateRequest updateRequest = new HotelReservationUpdateRequest();
    updateRequest.setCheckInDate(checkInDateRequest);
    updateRequest.setCheckOutDate(checkOutDateRequest);
    updateRequest.setNoOfGuests(reservation.getNoOfGuests());

    // Perform the update and ensure no exceptions are thrown
    HotelReservationDetailResponse response =
            updateReservationUseCase.update(reservation.getId(), updateRequest);

    // Verify that the reservation remains unchanged
    assertEquals(reservation.getCheckInDate(), response.getCheckInDate());
    assertEquals(reservation.getCheckOutDate(), response.getCheckOutDate());
    assertEquals(reservation.getNoOfGuests(), response.getNoOfGuests());
  }

  @Test
  public void testUpdateReservationWithValidFutureDates() throws BadRequestException {
    // Use valid future dates for updating the reservation
    HotelReservationUpdateRequest updateRequest = new HotelReservationUpdateRequest();
    LocalDate newCheckInDate = LocalDate.now().plusDays(15).plusMonths(1);
    LocalDate newCheckOutDate = LocalDate.now().plusDays(25).plusMonths(1);
    updateRequest.setCheckInDate(newCheckInDate.format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));
    updateRequest.setCheckOutDate(newCheckOutDate.format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter()));
    updateRequest.setNoOfGuests(4);

    // Perform the update
    HotelReservationDetailResponse response =
            updateReservationUseCase.update(reservation.getId(), updateRequest);

    // Verify that the updated values match the request
    assertEquals(newCheckInDate, response.getCheckInDate());
    assertEquals(newCheckOutDate, response.getCheckOutDate());
    assertEquals(4, response.getNoOfGuests());
  }
}
