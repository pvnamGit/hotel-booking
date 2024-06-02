package com.hrs.infrastructure.persistence.user;

import com.hrs.core.domain.user.User;
import com.hrs.core.repository.user.UserRepository;
import com.hrs.infrastructure.persistence.shared.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements UserRepository {
    @PersistenceContext
    EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public UserRepositoryImpl(EntityManager entityManager) {
        super(User.class);
    }
}
