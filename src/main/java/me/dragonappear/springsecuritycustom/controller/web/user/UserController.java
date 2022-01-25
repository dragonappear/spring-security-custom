package me.dragonappear.springsecuritycustom.controller.web.user;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.dto.AccountDto;
import me.dragonappear.springsecuritycustom.repository.RoleRepository;
import me.dragonappear.springsecuritycustom.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final RoleRepository roleRepository;

    @GetMapping("/users")
    public String createUser() {
        return "user/login/register";
    }

    @PostMapping("/users")
    public String createUser(AccountDto accountDto) {
        accountService.createUser(accountDto);
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "user/mypage";
    }
}
