package com.hrs.application.usecase.reservation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.hrs.core.domain.account.Account;
import com.hrs.core.domain.account.Authority;
import com.hrs.core.domain.account.SecurityAccountDetails;
import com.hrs.core.domain.account.SecurityCurrentUser;
import com.hrs.core.domain.reservation.HotelReservation;
import com.hrs.core.domain.user.User;
import com.hrs.core.repository.reservation.HotelReservationRepository;
import com.hrs.core.service.reservation.HotelReservationService;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CancelReservationUseCaseTest {

  @Mock private HotelReservationService hotelReservationService;

  @InjectMocks private CancelReservationUseCase cancelReservationUseCase;

  @Mock private HotelReservationRepository hotelReservationRepository;

  @Mock private SecurityCurrentUser currentUser;

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
  public void testCancelReservation_Success() {
    // Mock current user
    when(currentUser.getCurrentUser()).thenReturn(securityAccountDetails);

    // Mock repository methods
    when(hotelReservationRepository.authorizeUser(hotelReservation.getId(), user.getId()))
        .thenReturn(true);
    when(hotelReservationRepository.findById(hotelReservation.getId()))
        .thenReturn(hotelReservation);
    doAnswer(
            invocation -> {
              hotelReservation.setCancelledAt(Instant.now().toEpochMilli());
              hotelReservation.setUpdatedAt(Instant.now().toEpochMilli());
              return null;
            })
        .when(hotelReservationService)
        .cancelReservation(hotelReservation.getId());
    // Perform the test
    cancelReservationUseCase.cancel(hotelReservation.getId());

    // Verify the reservation was cancelled
    assertNotNull(hotelReservation.getCancelledAt());
    assertNotNull(hotelReservation.getUpdatedAt());
  }
}
