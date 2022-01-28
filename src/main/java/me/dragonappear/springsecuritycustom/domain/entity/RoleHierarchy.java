package me.dragonappear.springsecuritycustom.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@ToString(exclude = {"accounts","resources"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @Builder
@Getter
@Entity
public class RoleHierarchy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_hierarchy_id")
    private Long id;

    @Column(name = "child_name")
    private String childName;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_name", referencedColumnName = "child_name")
    private RoleHierarchy parentName;

    @OneToMany(mappedBy = "parentName", cascade = {CascadeType.ALL})
    private Set<RoleHierarchy> roleHierarchy = new HashSet<>();
}
