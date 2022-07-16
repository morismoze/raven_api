package com.raven.api.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class UserRequestDto {
    
    @NotNull
    @Size(max = 30)
    private String firstName;

    @NotNull
    @Size(max = 30)
    private String lastName;

    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String username;

    @NotNull
    @Size(max = 60)
    private String password;

}
