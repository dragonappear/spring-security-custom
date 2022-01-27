package me.dragonappear.springsecuritycustom.controller.api;

import me.dragonappear.springsecuritycustom.security.web.service.AccountContext;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @GetMapping("/api/test")
    public String test(Authentication authentication) {
        return ((AccountContext) authentication.getPrincipal()).getAccount().getId().toString();
    }
}
