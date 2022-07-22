package com.raven.api.response;

import java.sql.Timestamp;
import java.util.List;

import com.raven.api.model.Image;
import com.raven.api.model.ImageComment;
import com.raven.api.model.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserResponseDto {
    
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private List<Role> roles;

    private List<Image> images;

    private List<ImageComment> imageComments;

    private Timestamp createdAt;

    private Timestamp updatedAt;

}
