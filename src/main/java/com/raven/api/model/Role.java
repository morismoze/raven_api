package com.raven.api.model;

import com.raven.api.model.enums.RoleName;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ROLE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

}
