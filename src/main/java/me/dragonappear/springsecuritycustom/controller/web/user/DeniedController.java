package me.dragonappear.springsecuritycustom.controller.web.user;

import me.dragonappear.springsecuritycustom.security.web.service.AccountContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DeniedController {

    @GetMapping(value = {"/denied"})
    public String denied(@RequestParam(value = "exception", required = false) String exception,
                         Model model,
                         Authentication authentication) {

        AccountContext principal = (AccountContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("username", principal.getAccount().getUsername());
        model.addAttribute("exception", exception);
        return "user/login/denied";
    }
}
