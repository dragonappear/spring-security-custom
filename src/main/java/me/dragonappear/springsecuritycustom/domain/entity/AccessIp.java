package me.dragonappear.springsecuritycustom.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccessIp implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "ip_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String ipAddress;
}
