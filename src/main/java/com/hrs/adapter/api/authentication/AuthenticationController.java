package com.hrs.adapter.api.authentication;

import com.hrs.adapter.api.shared.BaseEntityResponse;
import com.hrs.application.usecase.authentication.SignInUseCase;
import com.hrs.application.usecase.authentication.SignUpUseCase;
import com.hrs.application.dto.authentication.request.SignInRequest;
import com.hrs.application.dto.authentication.request.SignUpRequest;
import com.hrs.application.dto.authentication.response.AuthenticationResponse;
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
    public BaseEntityResponse signUp(@RequestBody @Valid SignUpRequest req) {
        signUpUseCase.signUp(req);
        return BaseEntityResponse.success();
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public BaseEntityResponse signIn(@RequestBody @Valid SignInRequest req) {
        AuthenticationResponse response = signInUseCase.signIn(req);
        return BaseEntityResponse.success(response);
    }
}
