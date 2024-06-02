package com.hrs.core.service.authentication.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRESP {
    private String token;
}
