package com.hrs.application.usecase.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.hrs.core.domain.account.Account;
import com.hrs.core.domain.account.Authority;
import com.hrs.core.domain.account.SecurityAccountDetails;
import com.hrs.core.domain.account.SecurityCurrentUser;
import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.core.domain.reservation.HotelReservation;
import com.hrs.core.domain.user.User;
import com.hrs.core.repository.hotel.HotelRoomRepository;
import com.hrs.core.repository.reservation.HotelReservationRepository;
import com.hrs.core.service.reservation.HotelReservationService;
import com.hrs.core.service.reservation.request.HotelReservationUpdateRequest;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

public class UpdateReservationUseCaseTest {

  @Mock private HotelReservationService hotelReservationService;

  @InjectMocks private UpdateReservationUseCase updateReservationUseCase;

  @Mock private HotelReservationRepository hotelReservationRepository;

  @Mock private HotelRoomRepository hotelRoomRepository;

  @Mock private SecurityCurrentUser currentUser;

  @Mock private EntityManager entityManager;

  private Hotel hotel;
  private HotelRoom hotelRoom;
  private User user;
  private Account account;
  private SecurityAccountDetails securityAccountDetails;
  private HotelReservation hotelReservation;
  private final Long ID = 1L;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    // Initialize common objects
    hotel = new Hotel();
    hotel.setId(ID);

    hotelRoom = new HotelRoom();
    hotelRoom.setId(ID);
    hotelRoom.setPrice(100.00);

    user = new User();
    user.setId(ID);

    account = new Account();
    account.setId(ID);
    account.setEmail("test@gmail.com");
    account.setPassword("testPassword");
    account.setUser(user);
    account.setAuthority(Authority.USER);

    securityAccountDetails = SecurityAccountDetails.build(account);

    hotelReservation = new HotelReservation();
    hotelReservation.setId(ID);
    hotelReservation.setUser(user);
    hotelReservation.setHotel(hotel);
    hotelReservation.setHotelRoom(hotelRoom);
    hotelReservation.setCheckInDate(LocalDate.of(2024, 1, 1));
    hotelReservation.setCheckOutDate(LocalDate.of(2024, 1, 3));
    hotelReservation.setNoOfGuests(2);
  }

  @Test
  public void testUpdateReservation() throws BadRequestException {
    // Mock current user
    when(currentUser.getCurrentUser()).thenReturn(securityAccountDetails);

    // Mock hotel reservation update request
    HotelReservationUpdateRequest request = new HotelReservationUpdateRequest();
    request.setCheckInDate("02-01-2024");
    request.setCheckOutDate("04-01-2024");
    request.setNoOfGuests(3);
    request.setHotelRoomId(hotelRoom.getId());

    // Mock repository methods
    when(hotelReservationRepository.authorizeUser(hotelReservation.getId(), user.getId()))
        .thenReturn(true);
    when(hotelReservationRepository.isConflictReservation(
            hotelRoom.getId(),
            LocalDate.of(2024, 1, 2),
            LocalDate.of(2024, 1, 4),
            hotelReservation.getId()))
        .thenReturn(false);
    when(hotelRoomRepository.validateHotelRoom(hotel.getId(), hotelRoom.getId())).thenReturn(true);
    when(hotelReservationRepository.findById(hotelReservation.getId()))
        .thenReturn(hotelReservation);

    // Mock hotel reservation response
    HotelReservationDetailResponse expectedResponse =
        HotelReservationDetailResponse.builder().id(ID).build();

    // Mock hotel reservation service behavior
    when(hotelReservationService.updateReservation(hotelReservation.getId(), request))
        .thenReturn(expectedResponse);

    // Perform the test
    HotelReservationDetailResponse actualResponse =
        updateReservationUseCase.update(hotelReservation.getId(), request);

    // Verify the result
    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void testUpdateReservation_Conflict() throws BadRequestException {
    // Mock current user
    when(currentUser.getCurrentUser()).thenReturn(securityAccountDetails);

    // Mock hotel reservation update request
    HotelReservationUpdateRequest request = new HotelReservationUpdateRequest();
    request.setCheckInDate("02-01-2024");
    request.setCheckOutDate("04-01-2024");
    request.setNoOfGuests(3);
    request.setHotelRoomId(hotelRoom.getId());

    // Mock service to throw BadRequestException for conflict reservation
    when(hotelReservationService.updateReservation(hotelReservation.getId(), request))
        .thenThrow(
            new BadRequestException("Reservation dates conflict with existing reservations."));

    // Perform the test and verify exception
    assertThrows(
        BadRequestException.class,
        () -> {
          updateReservationUseCase.update(hotelReservation.getId(), request);
        });
  }

  @Test
  public void testUpdateReservation_Unauthorized() throws BadRequestException {
    // Mock current user
    when(currentUser.getCurrentUser()).thenReturn(securityAccountDetails);

    // Mock hotel reservation update request
    HotelReservationUpdateRequest request = new HotelReservationUpdateRequest();
    request.setCheckInDate("02-01-2024");
    request.setCheckOutDate("04-01-2024");
    request.setNoOfGuests(3);
    request.setHotelRoomId(hotelRoom.getId());

    // Mock repository methods
    when(hotelReservationRepository.authorizeUser(hotelReservation.getId(), user.getId()))
        .thenReturn(false);
    hotelReservationService.updateReservation(hotelReservation.getId(), request);
    when(hotelReservationService.updateReservation(hotelReservation.getId(), request))
        .thenThrow(new AccessDeniedException("Unauthorized"));
    // Perform the test and verify exception
    assertThrows(
        AccessDeniedException.class,
        () -> {
          updateReservationUseCase.update(hotelReservation.getId(), request);
        });
  }
}
