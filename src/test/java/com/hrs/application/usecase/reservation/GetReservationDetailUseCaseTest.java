package com.hrs.application.usecase.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.hrs.core.domain.account.Account;
import com.hrs.core.domain.account.Authority;
import com.hrs.core.domain.account.SecurityAccountDetails;
import com.hrs.core.domain.account.SecurityCurrentUser;
import com.hrs.core.domain.reservation.HotelReservation;
import com.hrs.core.domain.user.User;
import com.hrs.core.repository.reservation.HotelReservationRepository;
import com.hrs.core.service.reservation.HotelReservationService;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

public class GetReservationDetailUseCaseTest {

  @Mock private HotelReservationService hotelReservationService;

  @InjectMocks private GetReservationDetailUseCase getReservationDetailUseCase;

  @Mock private HotelReservationRepository hotelReservationRepository;

  @Mock private SecurityCurrentUser currentUser;

  @Mock private EntityManager entityManager;

  private HotelReservation hotelReservation;
  private User user;
  private Account account;
  private SecurityAccountDetails securityAccountDetails;
  private final Long ID = 1L;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    // Initialize common objects
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
  }

  @Test
  public void testGetReservationDetail_Success() {
    // Mock current user
    when(currentUser.getCurrentUser()).thenReturn(securityAccountDetails);

    // Mock repository methods
    when(hotelReservationRepository.authorizeUser(hotelReservation.getId(), user.getId()))
        .thenReturn(true);
    when(hotelReservationRepository.findById(hotelReservation.getId()))
        .thenReturn(hotelReservation);

    // Mock hotel reservation response
    HotelReservationDetailResponse expectedResponse =
        HotelReservationDetailResponse.builder().id(ID).build();

    // Mock hotel reservation service behavior
    when(hotelReservationService.getReservationDetail(hotelReservation.getId()))
        .thenReturn(expectedResponse);

    // Perform the test
    HotelReservationDetailResponse actualResponse =
        getReservationDetailUseCase.getDetail(hotelReservation.getId());

    // Verify the result
    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void testGetReservationDetail_Unauthorized() {
    User newUser = new User();
    newUser.setId(2L);
    HotelReservation newReservation = new HotelReservation();
    newReservation.setId(2L);
    newReservation.setUser(newUser);
    // Mock current user
    when(currentUser.getCurrentUser()).thenReturn(securityAccountDetails);

    // Mock repository methods
    when(hotelReservationRepository.authorizeUser(newReservation.getId(), user.getId()))
        .thenReturn(false);
    when(hotelReservationService.getReservationDetail(newReservation.getId()))
        .thenThrow(new AccessDeniedException("Unauthorized"));
    // Perform the test and verify exception
    assertThrows(
        AccessDeniedException.class,
        () -> {
          getReservationDetailUseCase.getDetail(newReservation.getId());
        });
  }

  @Test
  public void testGetReservationDetail_NotFound() {
    // Mock current user
    when(currentUser.getCurrentUser()).thenReturn(securityAccountDetails);

    // Mock repository methods
    when(hotelReservationRepository.authorizeUser(2L, user.getId())).thenReturn(false);
    when(hotelReservationRepository.findById(2L)).thenReturn(null);
    when(hotelReservationService.getReservationDetail(2L)).thenThrow(new EntityNotFoundException());
    // Perform the test and verify exception
    assertThrows(
        EntityNotFoundException.class,
        () -> {
          getReservationDetailUseCase.getDetail(2L);
        });
  }
}
