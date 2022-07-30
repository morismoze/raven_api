package com.raven.api.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "comment_vote")
@Getter
@Setter
@ToString
public class CommentVote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_comment_id")
    private PostComment postComment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public CommentVote() {
    }

    public CommentVote(final Long id, 
                       final PostComment postComment, 
                       final User user, 
                       final Timestamp createdAt, 
                       final Timestamp updatedAt) {
        this.id = id;
        this.postComment = postComment;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
