package com.hrs.application.usecase.authentication;

import com.hrs.core.service.authentication.AuthenticationService;
import com.hrs.core.service.authentication.request.SignUpREQ;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class SignUpUseCase {
  private final AuthenticationService authenticationService;

  public SignUpUseCase(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @SneakyThrows
  public void signUp(SignUpREQ req) {
    authenticationService.registerAccount(req);
  }
}
