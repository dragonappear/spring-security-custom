package me.dragonappear.springsecuritycustom.repository;

import me.dragonappear.springsecuritycustom.domain.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource,Long> {
    Resource findByResourceNameAndHttpMethod(String resourceName, String httpMethod);
    @Query("select r from Resource r join fetch r.roleSet where r.resourceType = 'url' order by r.orderNum desc")
    List<Resource> findAllResource();

    @Query("select r from Resource r join fetch r.roleSet where r.resourceType = 'method' order by r.orderNum desc")
    List<Resource> findAllMethodResource();

    @Query("select r from Resource r join fetch r.roleSet where r.resourceType = 'pointcut' order by r.orderNum desc")
    List<Resource> findAllPointcutResource();

}
