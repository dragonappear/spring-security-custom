package me.dragonappear.springsecuritycustom.security.web.factory;

import me.dragonappear.springsecuritycustom.service.SecurityResourceService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;

import java.util.LinkedHashMap;
import java.util.List;

public class MethodResourceMapFactoryBean implements FactoryBean<LinkedHashMap<String, List<ConfigAttribute>>> {
    private SecurityResourceService securityResourceService;
    private String resourceType;
    private LinkedHashMap<String, List<ConfigAttribute>> resourceMap;

    public MethodResourceMapFactoryBean(SecurityResourceService securityResourceService, String resourceType) {
        this.securityResourceService = securityResourceService;
        this.resourceType = resourceType;
    }

    @Override
    public LinkedHashMap<String, List<ConfigAttribute>> getObject() {
        if (resourceMap == null) {
            init();
        }
        return resourceMap;
    }

    private void init() {
        if ("method".equals(resourceType)) {
            resourceMap = securityResourceService.getMethodResourceList();
        }else if("pointcut".equals(resourceType)){
            resourceMap = securityResourceService.getPointcutResourceList();
        }

    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }
}
