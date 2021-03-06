package me.dragonappear.springsecuritycustom.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@ToString(exclude = "roleSet")
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
@Getter
@Entity
public class Resource implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Long id;
    @Column
    private String resourceName;
    @Column
    private String httpMethod;
    @Column
    private Long orderNum;
    @Column
    private String resourceType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_resources",
            joinColumns = {@JoinColumn(name = "resource_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roleSet = new HashSet<>();

    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }
}
