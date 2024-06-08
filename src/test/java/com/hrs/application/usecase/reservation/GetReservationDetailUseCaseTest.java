package com.hrs.application.usecase.reservation;

import static org.junit.jupiter.api.Assertions.*;

import com.hrs.core.domain.account.Account;
import com.hrs.core.domain.account.Authority;
import com.hrs.core.domain.user.User;
import com.hrs.core.repository.account.AccountRepository;
import com.hrs.core.repository.reservation.HotelReservationRepository;
import com.hrs.core.repository.user.UserRepository;
import com.hrs.core.service.reservation.request.HotelReservationCreateRequest;
import com.hrs.core.service.reservation.response.HotelReservationDetailResponse;
import java.time.LocalDate;
import javax.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class GetReservationDetailUseCaseTest {
  private final Long ID = 1L;
  @Autowired private GetReservationDetailUseCase getReservationDetailUseCase;
  @Autowired private UpdateReservationUseCase updateReservationUseCase;
  @Autowired private CreateReservationUseCase createReservationUseCase;
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private HotelReservationRepository hotelReservationRepository;
  @Autowired private AccountRepository accountRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;

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
  public void testGetReservationDetailSuccess() {
    HotelReservationDetailResponse response =
        getReservationDetailUseCase.getDetail(reservation.getId());
    assertNotNull(response);
    assertEquals(LocalDate.of(2024, 7, 1), response.getCheckInDate());
    assertEquals(LocalDate.of(2024, 7, 5), response.getCheckOutDate());
    assertEquals(2, response.getNoOfGuests());
  }

  @Test
  public void testGetReservationDetailThrowUnauthorized() {
    setUpNewAccount();
    // Perform the test and verify exception
    assertThrows(
        AccessDeniedException.class,
        () -> {
          getReservationDetailUseCase.getDetail(reservation.getId());
        });
  }

  @Test
  public void testGetReservationDetailThrowNotFound() {
    // Perform the test and verify exception
    assertThrows(
        EntityNotFoundException.class,
        () -> {
          getReservationDetailUseCase.getDetail(999L);
        });
  }

  private void setUpNewAccount() {
    Account account =
        Account.builder()
            .email("account+1@gmail.com")
            .password(passwordEncoder.encode("password"))
            .authority(Authority.USER)
            .build();
    accountRepository.persistAndFlush(account);
    User user = User.builder().firstName("First").lastName("Last").account(account).build();
    userRepository.persist(user);
    var auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken("account+1@gmail.com", "password"));
    SecurityContextHolder.getContext()
        .setAuthentication(auth); // Set authentication in security context
  }
}
