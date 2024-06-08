package com.hrs.application.usecase.reservation;

import static org.junit.jupiter.api.Assertions.*;

import com.hrs.core.repository.reservation.HotelReservationRepository;
import com.hrs.core.service.reservation.request.HotelReservationCreateRequest;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;
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
    request =
        HotelReservationCreateRequest.builder()
            .hotelId(1L)
            .hotelRoomId(1L)
            .checkInDate("01-07-2024")
            .checkOutDate("05-07-2024")
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
    assertEquals(LocalDate.of(2024, 7, 1), result.getCheckInDate());
    assertEquals(LocalDate.of(2024, 7, 5), result.getCheckOutDate());
    assertEquals(2, result.getNoOfGuests());
  }

  @Test
  public void testCreateReservationDateConflict() throws BadRequestException {
    // Create an initial reservation to create a conflict
    HotelReservationCreateRequest initialRequest =
        HotelReservationCreateRequest.builder()
            .hotelId(1L)
            .hotelRoomId(1L)
            .checkInDate("02-07-2024")
            .checkOutDate("04-07-2024")
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

    assertEquals("Room not belong to Hotel", exception.getMessage());
  }
}
