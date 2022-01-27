package me.dragonappear.springsecuritycustom.controller.web.admin;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.dto.RoleDto;
import me.dragonappear.springsecuritycustom.domain.entity.Role;
import me.dragonappear.springsecuritycustom.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class RoleController {
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @GetMapping(value = "/admin/roles")
    public String getRoles(Model model) {
        model.addAttribute("roles", roleService.getRoles());
        return "admin/role/list";
    }

    @GetMapping(value = "/admin/roles/register")
    public String viewRoles(Model model) {
        model.addAttribute("role", new RoleDto());
        return "admin/role/detail";
    }

    @PostMapping(value = "/admin/roles")
    public String createRole(RoleDto roleDto) {
        Role role = modelMapper.map(roleDto, Role.class);
        roleService.createRole(role);
        return "redirect:/admin/roles";
    }

    @GetMapping(value = "/admin/roles/{id}")
    public String getRole(@PathVariable String id, Model model) {
        Role role = roleService.getRole(Long.valueOf(id));
        model.addAttribute("role", modelMapper.map(role, RoleDto.class));

        return "admin/role/detail";
    }

    @GetMapping(value = "/admin/roles/delete/{id}")
    public String removeResources(@PathVariable String id, Model model) {
        Role role = roleService.getRole(Long.valueOf(id));
        roleService.deleteRole(Long.valueOf(id));

        return "redirect:/admin/resources";
    }
}

