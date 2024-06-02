package com.hrs.core.domain.account;

import com.hrs.core.domain.shared.BaseEntity;
import com.hrs.core.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "authority", nullable = false)
  private Authority authority = Authority.USER;

  @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
  private User user;
}
