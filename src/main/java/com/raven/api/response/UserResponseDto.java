package com.raven.api.response;

import java.sql.Timestamp;
import java.util.List;

import com.raven.api.model.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserResponseDto {
    
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private List<Role> roles;

    private Timestamp createdAt;

    private Timestamp updatedAt;

}
