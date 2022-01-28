package me.dragonappear.springsecuritycustom.repository;

import me.dragonappear.springsecuritycustom.domain.entity.AccessIp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessIpRepository extends JpaRepository<AccessIp,Long> {
    AccessIp findByIpAddress(String IpAddress);
}
