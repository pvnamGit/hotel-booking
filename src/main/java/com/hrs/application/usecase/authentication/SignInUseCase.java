package com.hrs.application.usecase.authentication;

import com.hrs.core.service.authentication.AuthenticationService;
import com.hrs.application.dto.authentication.request.SignInRequest;
import com.hrs.application.dto.authentication.response.AuthenticationResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignInUseCase {
  private final AuthenticationService authenticationService;

  @SneakyThrows
  public AuthenticationResponse signIn(SignInRequest req) {
    return authenticationService.signIn(req);
  }
}
