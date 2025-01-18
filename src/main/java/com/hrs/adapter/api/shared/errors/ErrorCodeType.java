package com.hrs.adapter.api.shared.errors;

import javax.persistence.metamodel.Type;

public interface ErrorCodeType extends Type<String> {
    PersistenceType getPersistenceType();
}
