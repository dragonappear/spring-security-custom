package me.dragonappear.springsecuritycustom.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    @Column
    private String roleName;
    @Column
    private String roleDesc;

    @ManyToMany(mappedBy = "accountRoles")
    private Set<Account> accounts = new HashSet<>();
    @ManyToMany(mappedBy = "roleSet")
    private Set<Resource> resources = new HashSet<>();
}
