package com.hrs.core.repository.account;

import com.hrs.core.domain.account.Account;
import com.hrs.core.repository.shared.BaseRepository;

public interface AccountRepository extends BaseRepository<Account, Long> {
    Account findByEmail(String email);
}
