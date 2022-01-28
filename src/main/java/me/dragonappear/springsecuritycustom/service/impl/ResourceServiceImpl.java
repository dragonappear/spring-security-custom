package me.dragonappear.springsecuritycustom.service.impl;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.entity.Resource;
import me.dragonappear.springsecuritycustom.repository.ResourceRepository;

import me.dragonappear.springsecuritycustom.service.ResourceService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;

    @Override
    public Resource getResource(Long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    @Override
    public List<Resource> getResource() {
        return resourceRepository.findAll(Sort.by(Sort.Order.asc("orderNum")));
    }

    @Transactional
    @Override
    public void createResource(Resource resource) {
        resourceRepository.save(resource);
    }

    @Transactional
    @Override
    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }
}
