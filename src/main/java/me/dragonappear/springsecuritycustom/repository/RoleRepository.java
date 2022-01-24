package me.dragonappear.springsecuritycustom.repository;

import me.dragonappear.springsecuritycustom.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByRoleName(String roleName);
}
