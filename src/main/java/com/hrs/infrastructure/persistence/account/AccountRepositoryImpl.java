package com.hrs.infrastructure.persistence.account;

import com.hrs.core.domain.account.Account;
import com.hrs.core.repository.account.AccountRepository;
import com.hrs.infrastructure.persistence.shared.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AccountRepositoryImpl extends BaseRepositoryImpl<Account, Long> implements AccountRepository {
    @PersistenceContext
    EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public AccountRepositoryImpl(EntityManager entityManager) {
        super(Account.class);
    }
    @Override
    public Account findByEmail(String email) {
        return getSingleResult("email", email);
    }
}
