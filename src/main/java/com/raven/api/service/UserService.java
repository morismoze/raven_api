package com.raven.api.service;

import com.raven.api.model.User;
import com.raven.api.model.enums.RoleName;

public interface UserService {

    User createUser(User user, RoleName roleName);

    User addRoleToUser(User user, RoleName roleName);

    User findUser(Long id);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findCurrent();

    void deleteUserById(Long id);

}
