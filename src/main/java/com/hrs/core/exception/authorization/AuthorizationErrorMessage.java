package com.hrs.core.exception.authorization;

import lombok.Getter;

@Getter
public enum AuthorizationErrorMessage {
    EMAIL_REGISTERED("Email is already registered");
    private final String message;

    AuthorizationErrorMessage(String message) {
        this.message = message;
    }
}
