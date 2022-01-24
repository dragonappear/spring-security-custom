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
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private Long age;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "account_roles",
            joinColumns = {@JoinColumn(name = "account_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> accountRoles = new HashSet<>();
}
