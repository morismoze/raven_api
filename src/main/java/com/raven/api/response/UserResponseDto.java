package com.raven.api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserResponseDto {
    
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private List<String> roleNames;

}
