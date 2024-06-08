package com.hrs.application.usecase.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hrs.core.repository.reservation.HotelReservationRepository;
import com.hrs.core.service.reservation.request.HotelReservationCreateRequest;
import com.hrs.core.service.reservation.request.HotelReservationUpdateRequest;
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
public class UpdateReservationUseCaseTest {
  private final Long ID = 1L;
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
    HotelReservationCreateRequest request =
        HotelReservationCreateRequest.builder()
            .hotelId(ID)
            .hotelRoomId(ID)
            .checkInDate("01-07-2024")
            .checkOutDate("05-07-2024")
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
    updateRequest.setCheckInDate("02-08-2024");
    updateRequest.setCheckOutDate("04-08-2024");
    updateRequest.setNoOfGuests(3);

    // Perform the test
    HotelReservationDetailResponse response =
        updateReservationUseCase.update(reservation.getId(), updateRequest);

    // Verify the result
    assertEquals(LocalDate.of(2024, 8, 2), response.getCheckInDate());
    assertEquals(LocalDate.of(2024, 8, 4), response.getCheckOutDate());
    assertEquals(response.getNoOfGuests(), 3);
  }

  @Test
  public void testUpdateReservationDateConflict() throws BadRequestException {
    var newReservation =
        createReservationUseCase.create(
            HotelReservationCreateRequest.builder()
                .hotelId(ID)
                .hotelRoomId(ID)
                .checkInDate("01-08-2024")
                .checkOutDate("05-08-2024")
                .noOfGuests(2)
                .build());

    HotelReservationUpdateRequest updateRequest = new HotelReservationUpdateRequest();
    updateRequest.setCheckInDate("01-07-2024");
    updateRequest.setCheckOutDate("04-07-2024");
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
}
