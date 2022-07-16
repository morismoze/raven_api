package com.raven.api.mapper;

import com.raven.api.response.UserResponseDto;
import org.mapstruct.Mapper;

import com.raven.api.model.User;
import com.raven.api.request.UserRequestDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto userToUserResponseDto(User user);

    User userRequestDtoToUser(UserRequestDto userRequestDto);
    
}
