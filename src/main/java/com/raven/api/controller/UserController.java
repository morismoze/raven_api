package com.raven.api.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.raven.api.service.UserService;
import com.raven.api.validation.UserRequestDtoValidator;
import com.raven.api.mapper.UserMapper;
import com.raven.api.model.User;
import com.raven.api.model.enums.RoleName;
import com.raven.api.request.UserRequestDto;
import com.raven.api.response.UserResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    private final UserMapper userMapper;

    private final UserRequestDtoValidator userRequestDtoValidator;

    @PostMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<UserResponseDto> getUser(@PathVariable final String id) {
        final User user = this.userService.findUser(Long.parseLong(id));
        UserResponseDto userResponseDto = this.userMapper.userToUserResponseDto(user);

        return ResponseEntity.ok().body(userResponseDto);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody final UserRequestDto userRequestDto,
                                               final BindingResult bindingResult) {
        userRequestDtoValidator.validate(userRequestDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        
        final User newUser = this.userMapper.userRequestDtoToUser(userRequestDto);
        final User createdUser = this.userService.createUser(newUser, RoleName.getDefault());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/create").toUriString());
        UserResponseDto userResponseDto = this.userMapper.userToUserResponseDto(createdUser);

        return ResponseEntity.created(uri).body(userResponseDto);
    }

}
