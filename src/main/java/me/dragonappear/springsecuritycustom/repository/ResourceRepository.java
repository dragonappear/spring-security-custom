package me.dragonappear.springsecuritycustom.repository;

import me.dragonappear.springsecuritycustom.domain.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource,Long> {
}
