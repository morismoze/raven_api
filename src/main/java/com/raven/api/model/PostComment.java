package com.raven.api.model;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_comment")
@Getter
@Setter
public class PostComment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "postComment", orphanRemoval = true)
    private Set<PostCommentUpvote> postCommentUpvotes;

    @OneToMany(mappedBy = "postComment", orphanRemoval = true)
    private Set<PostCommentDownvote> postCommentDownvotes;

    @OneToMany(mappedBy = "postComment", orphanRemoval = true)
    private Set<PostCommentReport> postCommentReports;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public PostComment() {
    }

    public PostComment(final Long id, 
                       final String comment, 
                       final Post post, 
                       final User user, 
                       final Set<PostCommentUpvote> postCommentUpvotes,
                       final Set<PostCommentDownvote> postCommentDownvotes,
                       final Set<PostCommentReport> postCommentReports,
                       final Timestamp createdAt, 
                       final Timestamp updatedAt) {
        this.id = id;
        this.comment = comment;
        this.post = post;
        this.user = user;
        this.postCommentUpvotes = Set.copyOf(postCommentUpvotes);
        this.postCommentDownvotes = Set.copyOf(postCommentDownvotes);
        this.postCommentReports = postCommentReports;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", comment='" + getComment() + "'" +
            ", post='" + getPost() + "'" +
            ", user='" + getUser() + "'" +
            ", postCommentUpvotes='" + getPostCommentUpvotes() + "'" +
            ", postCommentDownvotes='" + getPostCommentDownvotes() + "'" +
            ", postCommentReports='" + getPostCommentReports() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PostComment)) {
            return false;
        }
        PostComment postComment = (PostComment) o;
        return Objects.equals(id, postComment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
