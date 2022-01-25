package me.dragonappear.springsecuritycustom.controller.web.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageController {
    @GetMapping(value = {"/messages","/api/messages"})
    public String messages() {
        return "user/messages";
    }
}
