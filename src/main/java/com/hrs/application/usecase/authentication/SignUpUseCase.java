package com.hrs.application.usecase.authentication;

import com.hrs.core.service.authentication.AuthenticationService;
import com.hrs.application.dto.authentication.request.SignUpRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class SignUpUseCase {
  private final AuthenticationService authenticationService;

  public SignUpUseCase(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @SneakyThrows
  public void signUp(SignUpRequest req) {
    authenticationService.registerAccount(req);
  }
}
