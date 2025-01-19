package com.hrs.core.service.authentication;

import com.hrs.application.dto.authentication.request.SignInRequest;
import com.hrs.application.dto.authentication.request.SignUpRequest;
import com.hrs.application.dto.authentication.response.AuthenticationResponse;
import org.hibernate.exception.ConstraintViolationException;

public interface AuthenticationService {
    void registerAccount(SignUpRequest signUpRequest) throws ConstraintViolationException;
    AuthenticationResponse signIn(SignInRequest req);
}
