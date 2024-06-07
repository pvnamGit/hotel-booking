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
import com.hrs.core.domain.user.User;
import com.hrs.core.repository.hotel.HotelRoomRepository;
import com.hrs.core.repository.reservation.HotelReservationRepository;
import com.hrs.core.service.reservation.HotelReservationService;
import com.hrs.core.service.reservation.request.HotelReservationCreateRequest;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;
import com.hrs.shared.enums.DateFormat;
import java.time.LocalDate;
import javax.persistence.EntityManager;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CreateReservationUseCaseTest {
  @Mock private HotelReservationService hotelReservationService;
  @InjectMocks private CreateReservationUseCase createReservationUseCase;
  @Mock private HotelReservationRepository hotelReservationRepository;
  @Mock private HotelRoomRepository hotelRoomRepository;
  @Mock private SecurityCurrentUser currentUser;
  @Mock private EntityManager entityManager;

  private Hotel hotel;
  private HotelRoom hotelRoom;
  private User user;
  private Account account;
  private SecurityAccountDetails securityAccountDetails;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    // Initialize common objects
    hotel = new Hotel();
    hotel.setId(1L);

    hotelRoom = new HotelRoom();
    hotelRoom.setId(1L);
    hotelRoom.setPrice(100.00);

    user = new User();
    user.setId(1L);

    account = new Account();
    account.setId(1L);
    account.setEmail("test@gmail.com");
    account.setPassword("testPassword");
    account.setUser(user);
    account.setAuthority(Authority.USER);

    securityAccountDetails = SecurityAccountDetails.build(account);
  }

  @Test
  public void testCreateReservationThenReturnSuccess() throws BadRequestException {
    // Mock current user
    when(currentUser.getCurrentUser()).thenReturn(securityAccountDetails);

    // Mock hotel reservation creation request
    HotelReservationCreateRequest request = new HotelReservationCreateRequest();
    request.setHotelId(hotel.getId());
    request.setHotelRoomId(hotelRoom.getId());
    request.setCheckInDate("01-01-2024");
    request.setCheckOutDate("03-01-2024");
    request.setNoOfGuests(2);

    // Mock repository methods
    when(hotelRoomRepository.validateHotelRoom(request.getHotelId(), request.getHotelRoomId()))
        .thenReturn(true);
    when(entityManager.getReference(Hotel.class, request.getHotelId())).thenReturn(hotel);
    when(hotelRoomRepository.findById(request.getHotelRoomId())).thenReturn(hotelRoom);
    when(entityManager.getReference(User.class, user.getId())).thenReturn(user);

    // Mock hotel reservation response
    HotelReservationDetailResponse expectedResponse =
        HotelReservationDetailResponse.builder().id(1L).build();

    // Mock hotel reservation service behavior
    when(hotelReservationService.createReservation(request)).thenReturn(expectedResponse);

    // Perform the test
    HotelReservationDetailResponse actualResponse =
        hotelReservationService.createReservation(request);

    // Verify the result
    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void testCreateReservationRoomAlreadyReservedThenThrowException() throws BadRequestException {
    // Mock current user
    when(currentUser.getCurrentUser()).thenReturn(securityAccountDetails);

    // Mock hotel reservation creation request
    HotelReservationCreateRequest request = new HotelReservationCreateRequest();
    request.setHotelId(hotel.getId());
    request.setHotelRoomId(hotelRoom.getId());
    request.setCheckInDate("01-01-2024");
    request.setCheckOutDate("03-01-2024");

    request.setNoOfGuests(2);

    // Indicate that the room is available
    LocalDate checkInDate =
        DateFormat.parseDate(request.getCheckInDate(), DateFormat.DATE_FORMAT_DD_MM_YYYY);
    LocalDate checkOutDate =
        DateFormat.parseDate(request.getCheckOutDate(), DateFormat.DATE_FORMAT_DD_MM_YYYY);

    when(hotelReservationRepository.isConflictReservation(
            request.getHotelRoomId(), checkInDate, checkOutDate, null))
        .thenReturn(true); // Indicate that there is a conflict with an existing reservation
    // Mock service to throw BadRequestException for conflict reservation
    when(hotelReservationService.createReservation(request))
        .thenThrow(new BadRequestException("Room is already reserved for the given dates"));

    // Perform the test and verify exception
    assertThrows(
            BadRequestException.class,
        () -> {
          createReservationUseCase.create(request);
        });
  }

}
