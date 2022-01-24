package me.dragonappear.springsecuritycustom.service;

import me.dragonappear.springsecuritycustom.domain.dto.AccountDto;
import me.dragonappear.springsecuritycustom.domain.entity.Account;

import java.util.List;

public interface AccountService {
    void createUser(AccountDto AccountDto);
    void modifyUser(AccountDto accountDto);
    List<Account> getUsers();
    AccountDto getUser(Long id);
    void deleteUser(Long idx);
    void order();
}
