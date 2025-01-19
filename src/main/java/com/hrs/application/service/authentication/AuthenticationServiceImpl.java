package com.hrs.application.service.authentication;

import com.hrs.application.dto.authentication.request.SignInRequest;
import com.hrs.application.dto.authentication.request.SignUpRequest;
import com.hrs.application.dto.authentication.response.AuthenticationResponse;
import com.hrs.core.domain.account.Account;
import com.hrs.core.domain.account.Authority;
import com.hrs.core.domain.account.SecurityAccountDetails;
import com.hrs.core.domain.user.User;
import com.hrs.core.exception.authorization.AuthorizationErrorMessage;
import com.hrs.core.repository.account.AccountRepository;
import com.hrs.core.repository.user.UserRepository;
import com.hrs.core.service.authentication.AuthenticationService;
import com.hrs.core.service.jwt.JwtService;
import javax.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
  private final AccountRepository accountRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Override
  @Transactional
  @SneakyThrows
  public void registerAccount(SignUpRequest signUpRequest) throws ConstraintViolationException {
    try {
      Account account =
          Account.builder()
              .email(signUpRequest.getEmail())
              .password(passwordEncoder.encode(signUpRequest.getPassword()))
              .authority(Authority.USER)
              .build();
      accountRepository.persistAndFlush(account);
      User user =
          User.builder()
              .firstName(signUpRequest.getFirstName())
              .lastName(signUpRequest.getLastName())
              .account(account)
              .build();
      userRepository.persist(user);
    } catch (DataIntegrityViolationException exception) {
      throw new DataIntegrityViolationException(
          AuthorizationErrorMessage.EMAIL_REGISTERED.getMessage());
    }
  }

  @Override
  public AuthenticationResponse signIn(SignInRequest req) {
    Account account = accountRepository.findByEmail(req.getEmail());
    if (account == null) throw new EntityNotFoundException("Email not found");
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException(e.getMessage());
    }

    SecurityAccountDetails accountDetails = SecurityAccountDetails.build(account);
    String token = jwtService.generateToken(accountDetails);
    return AuthenticationResponse.builder().token(token).build();
  }
}
