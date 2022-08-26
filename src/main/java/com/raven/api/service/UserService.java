package com.raven.api.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.raven.api.model.Role;
import com.raven.api.model.User;
import com.raven.api.model.enums.RoleName;

public interface UserService {

    User createUser(User user, RoleName roleName);

    void resendActivationEmail(Long userId);

    void activate(String uuid);

    void sendPasswordResetEmail(String email);

    void resetPassword(String uuid, String password);

    Role findRole(RoleName roleName);

    User addRoleToUser(User user, RoleName roleName);

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    User findCurrent();

    void refreshToken(HttpServletRequest request, HttpServletResponse response);

    void deleteById(Long id);

}
