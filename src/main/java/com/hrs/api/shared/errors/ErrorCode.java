package com.hrs.api.shared.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.metamodel.Type;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ErrorCodeType {
    E001("E001", "Unauthenticated"),
    E002("E002", "Bad Request Payload"),
    E003("E003", "Unauthorized"),
    E004("E004", "Record not found"),
    E005("E005", "Internal Server Error"),
    E006("E006", "Invalid Request Payload"),
    E007("E007", "Constraint Violation"),
    E008("E008", "Bad Credentials");

    final String code;
    final String message;

    @Override
    public Type.PersistenceType getPersistenceType() {
        return null;
    }
    @Override
    public Class<String> getJavaType() {
        return null;
    }
}
