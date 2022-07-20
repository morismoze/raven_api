package com.raven.api.request;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class UserLoginRequestDto {

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

}
