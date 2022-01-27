package me.dragonappear.springsecuritycustom.controller.web.admin;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.dto.AccountDto;
import me.dragonappear.springsecuritycustom.service.AccountService;
import me.dragonappear.springsecuritycustom.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserManagerController {
    private final AccountService accountService;
    private final RoleService roleService;

    @GetMapping(value = "/admin/accounts")
    public String getUsers(Model model) {
        model.addAttribute("accounts", accountService.getUsers());
        return "admin/user/list";
    }

    @PostMapping(value = "/admin/accounts")
    public String modifyUser(AccountDto accountDto) {
        accountService.modifyUser(accountDto);
        return "redirect:/admin/accounts";
    }

    @GetMapping(value = "/admin/accounts/{id}")
    public String getUser(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("account", accountService.getUser(id));
        model.addAttribute("roleList", roleService.getRoles());
        return "admin/user/detail";
    }

    @GetMapping(value = "/admin/accounts/delete/{id}")
    public String removeUser(@PathVariable(value = "id") Long id, Model model) {
        accountService.deleteUser(id);
        return "redirect:/admin/users";
    }
}

