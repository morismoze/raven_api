package com.raven.api.service.impl;

import com.raven.api.exception.EntryNotFoundException;
import com.raven.api.exception.NotLoggedInException;
import com.raven.api.model.Role;
import com.raven.api.model.User;
import com.raven.api.model.enums.RoleName;
import com.raven.api.repository.RoleRepository;
import com.raven.api.repository.UserRepository;
import com.raven.api.security.jwt.AuthUtils;
import com.raven.api.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    
    private final MessageSourceAccessor accessor;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user, RoleName roleName) {
        User userWithRole = this.addRoleToUser(user, roleName);
        final String plainPassword = userWithRole.getPassword();
        userWithRole.setPassword(passwordEncoder.encode(plainPassword));

        return this.userRepository.save(userWithRole);
    }

    @Override
    public User addRoleToUser(User user, RoleName roleName) {
        Optional<Role> roleOptional = this.roleRepository.findByRoleName(roleName);

        if (roleOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("user.roleName.notValid", new Object[]{roleName}));
        }
        
        user.getRoles().add(roleOptional.get());

        return user;
    }

    @Override
    public User findUser(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("user.notFound"));
        }

        return userOptional.get();
    }

    @Override
    public User findUserByUsername(String username) {
        Optional<User> userOptional = this.userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("user.notFound"));
        }

        return userOptional.get();
    }

    @Override
    public User findUserByEmail(String email) {
        Optional<User> userOptional = this.userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("user.notFound"));
        }

        return userOptional.get();
    }

    @Override
    public User findCurrent() {
        final Optional<String> username = AuthUtils.getCurrentUserUsername();
        
        if (username.isEmpty()) {
            throw new NotLoggedInException(accessor.getMessage("noLogin"));
        }

        return findUserByUsername(username.get());
    }

    @Override
    public void deleteUserById(Long id) {
        this.userRepository.deleteById(id);
    }


}
