package me.dragonappear.springsecuritycustom.service.impl;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.entity.RoleHierarchy;
import me.dragonappear.springsecuritycustom.repository.RoleHierarchyRepository;
import me.dragonappear.springsecuritycustom.service.RoleHierarchyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RoleHierarchyServiceImpl implements RoleHierarchyService {
    private final RoleHierarchyRepository roleHierarchyRepository;

    @Override
    public String findAllHierarchy() {
        List<RoleHierarchy> roleHierarchies = roleHierarchyRepository.findAll();

        Iterator<RoleHierarchy> iterator = roleHierarchies.iterator();
        StringBuilder concatedRoles = new StringBuilder();
        while (iterator.hasNext()) {
            RoleHierarchy roleHierarchy = iterator.next();
            if (roleHierarchy.getParentName() != null) {
                concatedRoles.append(roleHierarchy.getParentName().getName());
                concatedRoles.append(" > ");
                concatedRoles.append(roleHierarchy.getName());
                concatedRoles.append("\n");
            }
        }
        return concatedRoles.toString();
    }
}
