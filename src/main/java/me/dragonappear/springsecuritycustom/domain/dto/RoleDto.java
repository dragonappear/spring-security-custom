package me.dragonappear.springsecuritycustom.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDto {
    private String id;
    private String roleName;
    private String roleDesc;
}
