package com.hrs.application.usecase.authentication;

import com.hrs.core.service.authentication.AuthenticationService;
import com.hrs.core.service.authentication.request.SignInREQ;
import com.hrs.core.service.authentication.response.AuthenticationRESP;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class SignInUseCase {
  private final AuthenticationService authenticationService;

  public SignInUseCase(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @SneakyThrows
  public AuthenticationRESP signIn(SignInREQ req) {
    return authenticationService.signIn(req);
  }
}
