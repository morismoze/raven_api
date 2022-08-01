package com.raven.api.model;

import lombok.*;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "`user`")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, unique = true, name = "username")
    private String username;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<PostView> postViews;

    @OneToMany(mappedBy = "user")
    private List<PostUpvote> postUpvotes;

    @OneToMany(mappedBy = "user")
    private List<PostDownvote> postDownvotes;

    @OneToMany(mappedBy = "user")
    private List<PostComment> postComments;

    @OneToMany(mappedBy = "user")
    private List<PostCommentUpvote> postCommentUpvotes;

    @OneToMany(mappedBy = "user")
    private List<PostCommentDownvote> postCommentDownvotes;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public User() {
    }

    public User(final Long id, 
                final String firstName, 
                final String lastName, 
                final String email, 
                final String password, 
                final String username, 
                final List<Role> roles, 
                final List<Post> posts, 
                final List<PostView> postViews, 
                final List<PostUpvote> postUpvotes, 
                final List<PostDownvote> postDownvotes, 
                final List<PostComment> postComments, 
                final List<PostCommentUpvote> postCommentUpvotes, 
                final List<PostCommentDownvote> postCommentDownvotes, 
                final Timestamp createdAt, 
                final Timestamp updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.roles = List.copyOf(roles);
        this.posts = List.copyOf(posts);
        this.postViews = List.copyOf(postViews);
        this.postUpvotes = List.copyOf(postUpvotes);
        this.postDownvotes = List.copyOf(postDownvotes);
        this.postComments = List.copyOf(postComments);
        this.postCommentUpvotes = List.copyOf(postCommentUpvotes);
        this.postCommentDownvotes = List.copyOf(postCommentDownvotes);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", username='" + getUsername() + "'" +
            ", roles='" + getRoles() + "'" +
            ", posts='" + getPosts() + "'" +
            ", postViews='" + getPostViews() + "'" +
            ", postUpvotes='" + getPostUpvotes() + "'" +
            ", postDownvotes='" + getPostDownvotes() + "'" +
            ", postComments='" + getPostComments() + "'" +
            ", postCommentUpvotes='" + getPostCommentUpvotes() + "'" +
            ", postCommentDownvotes='" + getPostCommentDownvotes() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }

}
