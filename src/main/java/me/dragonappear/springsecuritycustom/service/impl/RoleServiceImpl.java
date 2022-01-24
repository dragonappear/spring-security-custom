package me.dragonappear.springsecuritycustom.service.impl;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.entity.Role;
import me.dragonappear.springsecuritycustom.repository.RoleRepository;
import me.dragonappear.springsecuritycustom.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRole(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    @Override
    public void createRole(Role role) {
        roleRepository.save(role);
    }

    @Transactional
    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
