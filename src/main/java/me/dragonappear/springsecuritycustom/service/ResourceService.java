package me.dragonappear.springsecuritycustom.service;

import me.dragonappear.springsecuritycustom.domain.entity.Resource;

import java.util.List;

public interface ResourceService {
    Resource getResources(Long id);
    List<Resource> getResources();
    void createResources(Resource resource);
    void deleteResources(Long id);
}
