package me.dragonappear.springsecuritycustom.service;

import me.dragonappear.springsecuritycustom.domain.entity.Resource;

import java.util.List;

public interface ResourceService {
    Resource getResource(Long id);
    List<Resource> getResource();
    void createResource(Resource resource);
    void deleteResource(Long id);
}
