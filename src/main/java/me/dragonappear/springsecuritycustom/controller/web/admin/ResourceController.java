package me.dragonappear.springsecuritycustom.controller.web.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.dragonappear.springsecuritycustom.domain.dto.ResourceDto;
import me.dragonappear.springsecuritycustom.domain.entity.Resource;
import me.dragonappear.springsecuritycustom.domain.entity.Role;
import me.dragonappear.springsecuritycustom.repository.RoleRepository;
import me.dragonappear.springsecuritycustom.service.ResourceService;
import me.dragonappear.springsecuritycustom.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ResourceController {

    private final ResourceService resourceService;
    private final RoleRepository roleRepository;
    private final RoleService roleService;


    @GetMapping(value = "/admin/resources")
    public String getResources(Model model) {
        List<Resource> resources = resourceService.getResources();
        model.addAttribute("resources", resources);
        return "admin/resource/list";
    }

    @PostMapping(value = "/admin/resources")
    public String createResources(ResourceDto resourceDto) throws Exception {
        Role role = roleRepository.findByRoleName(resourceDto.getRoleName());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        Resource resource = resourceDto.toEntity();
        resource.setRoleSet(roles);
        resourceService.createResources(resource);
        return "redirect:/admin/resources";
    }

    @GetMapping(value = "/admin/resources/register")
    public String viewRoles(Model model) {
        List<Role> roles = roleService.getRoles();
        model.addAttribute("roleList", roles);

        ResourceDto resource = new ResourceDto();
        Set<Role> roleSet = new HashSet<>();
        /*roleSet.add(new Role());*/
        roleSet.add(null);
        resource.setRoleSet(roleSet);
        model.addAttribute("resources", resource);

        return "admin/resource/detail";
    }

    @GetMapping(value = "/admin/resources/{id}")
    public String getResources(@PathVariable String id, Model model) {
        List<Role> roles = roleService.getRoles();
        model.addAttribute("roleList", roles);

        Resource resources = resourceService.getResources(Long.valueOf(id));
        ModelMapper modelMapper = new ModelMapper();

        ResourceDto resourcesDto = modelMapper.map(resources, ResourceDto.class);
        model.addAttribute("resources", resourcesDto);

        return "admin/resource/detail";
    }

    @GetMapping(value="/admin/resources/delete/{id}")
    public String removeResources(@PathVariable String id, Model model) throws Exception {

        Resource resources = resourceService.getResources(Long.valueOf(id));
        resourceService.deleteResources(Long.valueOf(id));

        return "redirect:/admin/resources";
    }
}
