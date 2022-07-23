package com.raven.api.model;

import com.raven.api.model.enums.RoleName;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Getter
@Setter
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleName roleName;

    public Role() {
    }

    public Role(final Long id, 
                final RoleName roleName) {
        this.id = id;
        this.roleName = roleName;
    }

}
