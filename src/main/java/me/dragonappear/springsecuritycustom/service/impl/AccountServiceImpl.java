package me.dragonappear.springsecuritycustom.service.impl;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.dto.AccountDto;
import me.dragonappear.springsecuritycustom.domain.entity.Account;
import me.dragonappear.springsecuritycustom.domain.entity.Role;
import me.dragonappear.springsecuritycustom.repository.AccountRepository;
import me.dragonappear.springsecuritycustom.repository.RoleRepository;
import me.dragonappear.springsecuritycustom.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public void createUser(AccountDto accountDto) {
        Account account = accountDto.toEntity();
        Role role = roleRepository.findByRoleName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        account.setAccountRoles(roles);
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void modifyUser(AccountDto accountDto) {
        Account account = accountDto.toEntity();
        if (account.getAccountRoles() != null) {
            Set<Role> roles = new HashSet<>();
            accountDto.getAccountRoles().forEach(
                    role -> {
                        roles.add(roleRepository.findByRoleName(role));
                    }
            );
            account.setAccountRoles(roles);
        }
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        accountRepository.save(account);
    }

    @Override
    public List<Account> getUsers() {
        return accountRepository.findAll();
    }

    @Override
    public AccountDto getUser(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        List<String> roles = account.getAccountRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toList());
        accountDto.setAccountRoles(roles);
        return accountDto;
    }

    @Transactional
    @Override
    public void deleteUser(Long idx) {
        accountRepository.deleteById(idx);
    }

    @Override
    public void order() {

    }
}
