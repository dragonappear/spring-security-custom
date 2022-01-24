package me.dragonappear.springsecuritycustom.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.entity.Account;

import java.util.List;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Data
public class AccountDto {
    private String id;
    private String username;
    private String email;
    private int age;
    private String password;
    private List<String> accountRoles;

    public Account toEntity() {
        return Account.builder()
                .id(Long.valueOf(this.getId()))
                .username(this.getUsername())
                .email(this.getEmail())
                .age(this.getAge())
                .password(this.getPassword())
                .build();
    }
}
