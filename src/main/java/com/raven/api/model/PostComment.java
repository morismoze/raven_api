package com.raven.api.model;

import java.sql.Timestamp;
import java.util.List;

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
import lombok.ToString;

@Entity
@Table(name = "post_comment")
@Getter
@Setter
@ToString
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

    @OneToMany(mappedBy = "postComment")
    private List<PostCommentUpvote> postCommentUpvotes;

    @OneToMany(mappedBy = "postComment")
    private List<PostCommentDownvote> postCommentDownvotes;

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
                       final List<PostCommentUpvote> postCommentUpvotes,
                       final List<PostCommentDownvote> postCommentDownvotes,
                       final Timestamp createdAt, 
                       final Timestamp updatedAt) {
        this.id = id;
        this.comment = comment;
        this.post = post;
        this.user = user;
        this.postCommentUpvotes = List.copyOf(postCommentUpvotes);
        this.postCommentDownvotes = List.copyOf(postCommentDownvotes);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
