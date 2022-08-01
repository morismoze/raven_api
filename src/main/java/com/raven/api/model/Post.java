package com.raven.api.model;

import java.sql.Timestamp;
import java.util.List;

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

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "web_id")
    private String webId;

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

    @OneToMany(mappedBy = "post")
    private List<PostView> postViews;

    @ManyToMany
    @JoinTable(name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @OneToMany(mappedBy = "post")
    private List<PostDownvote> postDownvotes;

    @OneToMany(mappedBy = "post")
    private List<PostUpvote> postUpvotes;

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
                final List<PostView> postViews,
                final List<Tag> tags,
                final List<PostUpvote> postUpvotes, 
                final List<PostDownvote> postDownvotes, 
                final List<PostComment> postComments, 
                final Timestamp createdAt, 
                final Timestamp updatedAt) {
        this.id = id;
        this.cover = cover;
        this.user = user;
        this.title = title;
        this.description = description;
        this.postViews = postViews;
        this.tags = tags;
        this.postUpvotes = List.copyOf(postUpvotes);
        this.postDownvotes = List.copyOf(postDownvotes);
        this.postComments = List.copyOf(postComments);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", webId='" + getWebId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", mature='" + isMature() + "'" +
            ", cover='" + getCover() + "'" +
            ", user='" + getUser() + "'" +
            ", postViews='" + getPostViews() + "'" +
            ", tags='" + getTags() + "'" +
            ", postDownvotes='" + getPostDownvotes() + "'" +
            ", postUpvotes='" + getPostUpvotes() + "'" +
            ", postComments='" + getPostComments() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }

}
