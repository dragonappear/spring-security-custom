package me.dragonappear.springsecuritycustom.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.entity.Resource;
import me.dragonappear.springsecuritycustom.domain.entity.Role;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceDto {
    private String id;
    private String resourceName;
    private String httpMethod;
    private Long orderNum;
    private String resourceType;
    private String roleName;
    private Set<Role> roleSet;

    public Resource toEntity() {
        return Resource.builder()
                .resourceName(resourceName)
                .httpMethod(httpMethod)
                .orderNum(orderNum)
                .resourceType(resourceType)
                .roleSet(roleSet)
                .build();
    }

}
