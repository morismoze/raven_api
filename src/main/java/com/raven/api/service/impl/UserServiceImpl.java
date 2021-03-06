package com.raven.api.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raven.api.exception.EntryNotFoundException;
import com.raven.api.exception.UnauthorizedException;
import com.raven.api.model.Image;
import com.raven.api.model.ImageComment;
import com.raven.api.model.Role;
import com.raven.api.model.User;
import com.raven.api.model.enums.RoleName;
import com.raven.api.repository.RoleRepository;
import com.raven.api.repository.UserRepository;
import com.raven.api.security.jwt.AuthUtils;
import com.raven.api.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    
    private final MessageSourceAccessor accessor;

    private final PasswordEncoder passwordEncoder;

    private final String TOKEN_TYPE = "Bearer ";

    @Value(value = "${jwt.secret}")
	private String secret;

    @Value(value = "${jwt.claim}")
    private String claim;

    @Value(value = "${jwt.access-token-expiration-time-millis}")
	private Long accessTokenExpirationTimeMillis;

    @Override
    public User createUser(final User user, final RoleName roleName) {
        final List<Role> roles = new ArrayList<>();
        final List<Image> images = new ArrayList<>();
        final List<ImageComment> imageComments = new ArrayList<>();
        final String plainPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(plainPassword));
        roles.add(findRole(roleName));
        user.setRoles(roles);
        user.setImages(images);
        user.setImageComments(imageComments);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return this.userRepository.save(user);
    }

    @Override
    public Role findRole(final RoleName roleName) {
        final Optional<Role> roleOptional = roleRepository.findByRoleName(roleName);
        if (roleOptional.isEmpty()) {
            throw new EntryNotFoundException(
                    accessor.getMessage("user.roleName.notValid", new Object[]{roleName}));
        }
        return roleOptional.get();
    }

    @Override
    public User addRoleToUser(final User user, final RoleName roleName) {
        final Optional<Role> roleOptional = this.roleRepository.findByRoleName(roleName);

        if (roleOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("user.roleName.notValid", new Object[]{roleName}));
        }
        
        user.getRoles().add(roleOptional.get());

        return user;
    }

    @Override
    public User findUser(final Long id) {
        final Optional<User> userOptional = this.userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("user.notFound"));
        }

        return userOptional.get();
    }

    @Override
    public User findUserByUsername(final String username) {
        final Optional<User> userOptional = this.userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("user.notFound"));
        }

        return userOptional.get();
    }

    @Override
    public User findUserByEmail(final String email) {
        final Optional<User> userOptional = this.userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("user.notFound"));
        }

        return userOptional.get();
    }

    @Override
    public User findCurrent() {
        final Optional<String> username = AuthUtils.getCurrentUserUsername();
        
        if (username.isEmpty()) {
            throw new UnauthorizedException(accessor.getMessage("user.noLogin"));
        }

        return findUserByUsername(username.get());
    }

    @Override
    public void deleteUserById(final Long id) {
        if (id == null) {
            throw new EntryNotFoundException(accessor.getMessage("user.id.empty"));
        }
        this.userRepository.deleteById(id);
    }

    @Override
    public void refreshToken(final HttpServletRequest request, final HttpServletResponse response) {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(this.TOKEN_TYPE)) {
            try {
                String refreshToken = authorizationHeader.substring(TOKEN_TYPE.length());
                Algorithm algorithm = Algorithm.HMAC256(this.secret.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                User user = this.findUserByUsername(username);
                String accessToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(this.accessTokenExpirationTimeMillis))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim(this.claim, user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()))
                    .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (JWTCreationException e) {
                e.printStackTrace();
            } catch (JWTVerificationException e) {
                e.printStackTrace();
                try {
                    response.sendError(HttpStatus.UNAUTHORIZED.value());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            throw new UnauthorizedException(this.accessor.getMessage("user.missingToken"));
        }        
    }

}
