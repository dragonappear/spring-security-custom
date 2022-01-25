package me.dragonappear.springsecuritycustom.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@ToString(exclude = {"accountRoles"})
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
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
    private String provider;
    @Column
    private String providerId;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "account_roles",
            joinColumns = {@JoinColumn(name = "account_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> accountRoles = new HashSet<>();

    public void setAccountRoles(Set<Role> accountRoles) {
        this.accountRoles = accountRoles;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
