package com.raven.api.mapper;

import com.raven.api.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.raven.api.model.User;
import com.raven.api.request.UserRequestDto;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponseDto userUserResponseDtoMapper(User user);

    User userRequestDtoUserMapper(UserRequestDto userRequestDto);
    
}
