package com.hrs.application.usecase.reservation;

import static org.junit.jupiter.api.Assertions.*;

import com.hrs.core.domain.account.Account;
import com.hrs.core.domain.account.Authority;
import com.hrs.core.domain.user.User;
import com.hrs.core.repository.account.AccountRepository;
import com.hrs.core.repository.reservation.HotelReservationRepository;
import com.hrs.core.repository.user.UserRepository;
import com.hrs.application.dto.reservation.request.HotelReservationCreateRequest;
import com.hrs.application.dto.reservation.response.HotelReservationDetailResponse;
import java.time.LocalDate;
import javax.persistence.EntityNotFoundException;

import com.hrs.shared.enums.DateFormat;
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

  LocalDate checkInDate, checkOutDate;
  String checkInDateRequest, checkOutDateRequest;
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
    checkInDate = LocalDate.now().plusDays(1).plusMonths(1);
    checkOutDate = LocalDate.now().plusDays(10).plusMonths(1);
    checkInDateRequest = checkInDate.format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter());
    checkOutDateRequest  = checkOutDate.format(DateFormat.DATE_FORMAT_DD_MM_YYYY.getFormatter());

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
  public void testGetReservationDetailSuccess() {
    HotelReservationDetailResponse response =
        getReservationDetailUseCase.getDetail(reservation.getId());
    assertNotNull(response);
    assertEquals(checkInDate, response.getCheckInDate());
    assertEquals(checkOutDate, response.getCheckOutDate());
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
