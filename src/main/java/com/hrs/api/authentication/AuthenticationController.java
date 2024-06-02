package com.hrs.api.authentication;

import com.hrs.api.shared.BaseEntityResponse;
import com.hrs.application.usecase.authentication.SignInUseCase;
import com.hrs.application.usecase.authentication.SignUpUseCase;
import com.hrs.core.service.authentication.request.SignInREQ;
import com.hrs.core.service.authentication.request.SignUpREQ;
import com.hrs.core.service.authentication.response.AuthenticationRESP;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("${url.prefix}/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final SignUpUseCase signUpUseCase;
    private final SignInUseCase signInUseCase;
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseEntityResponse signUp(@RequestBody @Valid SignUpREQ req) {
        signUpUseCase.signUp(req);
        return BaseEntityResponse.success();
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public BaseEntityResponse signIn(@RequestBody @Valid SignInREQ req) {
        AuthenticationRESP response = signInUseCase.signIn(req);
        return BaseEntityResponse.success(response);
    }
}
