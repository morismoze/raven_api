package com.raven.api.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.raven.api.model.Role;
import com.raven.api.model.User;
import com.raven.api.model.enums.RoleName;

public interface UserService {

    User createUser(User user, RoleName roleName);

    Role findRole(RoleName roleName);

    User addRoleToUser(User user, RoleName roleName);

    User findUser(Long id);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findCurrent();

    void refreshToken(HttpServletRequest request, HttpServletResponse response);

    void deleteUserById(Long id);

}
