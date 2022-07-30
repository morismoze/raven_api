package com.raven.api.model;

import lombok.*;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "`user`")
@Getter
@Setter
@ToString
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
    private List<PostVote> postVotes;

    @OneToMany(mappedBy = "user")
    private List<PostComment> postComments;

    @OneToMany(mappedBy = "user")
    private List<CommentVote> commentVotes;

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
                final List<PostVote> postVotes, 
                final List<PostComment> postComments, 
                final List<CommentVote> commentVotes, 
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
        this.postVotes = List.copyOf(postVotes);
        this.postComments = List.copyOf(postComments);
        this.commentVotes = List.copyOf(commentVotes);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
