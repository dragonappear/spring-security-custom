package me.dragonappear.springsecuritycustom.repository;

import me.dragonappear.springsecuritycustom.domain.entity.RoleHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHierarchyRepository extends JpaRepository<RoleHierarchy, Long> {
    RoleHierarchy findByName(String name);
}

