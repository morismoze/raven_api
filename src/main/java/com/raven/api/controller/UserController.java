package com.raven.api.controller;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.raven.api.service.UserService;
import com.raven.api.validation.UserRequestDtoValidator;
import com.raven.api.mapper.UserMapper;
import com.raven.api.model.User;
import com.raven.api.model.enums.RoleName;
import com.raven.api.request.UserRequestDto;
import com.raven.api.response.Response;
import com.raven.api.response.UserResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    private final UserMapper userMapper;

    private final UserRequestDtoValidator userRequestDtoValidator;

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Response<?>> getUser(@PathVariable final Long id) {
        final User user = this.userService.findById(id);
        final UserResponseDto userResponseDto = this.userMapper.userUserResponseDtoMapper(user);

        return ResponseEntity.ok().body(Response.build(userResponseDto));
    }

    @PostMapping("/create")
    public ResponseEntity<Response<?>> createUser(@RequestBody final UserRequestDto userRequestDto,
        final BindingResult errors) {
        userRequestDtoValidator.validate(userRequestDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(Response.build(errors));
        }
        
        final User newUser = this.userMapper.userRequestDtoUserMapper(userRequestDto);
        final User createdUser = this.userService.createUser(newUser, RoleName.getDefault());
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/create").toUriString());
        final UserResponseDto userResponseDto = this.userMapper.userUserResponseDtoMapper(createdUser);

        return ResponseEntity.created(uri).body(Response.build(userResponseDto));
    }

    @PutMapping("/activate")
    public ResponseEntity<Response<?>> activate(@RequestParam final String uuid) {
        this.userService.activate(uuid);
        return ResponseEntity.ok(Response.build(null, false));
    }

    @GetMapping("/current")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Response<?>> getCurrentUser() {
        final User user = this.userService.findCurrent();
        final UserResponseDto userResponseDto = this.userMapper.userUserResponseDtoMapper(user);

        return ResponseEntity.ok().body(Response.build(userResponseDto));
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        this.userService.refreshToken(request, response);
    }

}
