package me.dragonappear.springsecuritycustom.repository;

import me.dragonappear.springsecuritycustom.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByUsername(String username);
}
