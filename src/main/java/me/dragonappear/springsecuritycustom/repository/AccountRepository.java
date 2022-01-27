package me.dragonappear.springsecuritycustom.repository;

import me.dragonappear.springsecuritycustom.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByUsername(String username);
    Account findByEmail(String email);
    Optional<Account> findByProviderAndProviderId(String provider, String providerId);
}
