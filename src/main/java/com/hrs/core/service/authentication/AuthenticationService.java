package com.hrs.core.service.authentication;

import com.hrs.core.service.authentication.request.SignInREQ;
import com.hrs.core.service.authentication.request.SignUpREQ;
import com.hrs.core.service.authentication.response.AuthenticationRESP;
import org.hibernate.exception.ConstraintViolationException;

public interface AuthenticationService {
    void registerAccount(SignUpREQ signUpRequest) throws ConstraintViolationException;
    AuthenticationRESP signIn(SignInREQ req);
}
