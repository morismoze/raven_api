package com.raven.api.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "post")
@Getter
@Setter
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "mature")
    private boolean mature;

    @OneToOne(mappedBy = "post")
    private Cover cover;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @OneToMany(mappedBy = "post")
    private List<PostVote> postVotes;

    @OneToMany(mappedBy = "post")
    private List<PostComment> postComments;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Post() {
    }

    public Post(final Long id, 
                final Cover cover, 
                final User user, 
                final String title, 
                final String description, 
                final List<PostVote> postVotes, 
                final List<PostComment> postComments, 
                final Timestamp createdAt, 
                final Timestamp updatedAt) {
        this.id = id;
        this.cover = cover;
        this.user = user;
        this.title = title;
        this.description = description;
        this.postVotes = List.copyOf(postVotes);
        this.postComments = List.copyOf(postComments);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
