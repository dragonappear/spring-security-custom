package me.dragonappear.springsecuritycustom.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@ToString(exclude = {"parentName","roleHierarchy"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class RoleHierarchy implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_hierarchy_id")
    private Long id;

    @Column(name = "role_hierarchy_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_name", referencedColumnName = "role_hierarchy_name")
    private RoleHierarchy parentName;

    @OneToMany(mappedBy = "parentName")
    private Set<RoleHierarchy> childRoleHierarchy = new HashSet<>();

    public void setParentName(RoleHierarchy parentRoleHierarchy) {
        this.parentName = parentRoleHierarchy;
    }
}
