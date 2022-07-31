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
@Table(name = "post_vote")
@Getter
@Setter
@ToString
public class PostVote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "upvotes")
    private Integer upvotes;

    @Column(name = "downvotes")
    private Integer downvotes;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public PostVote() {
    }

    public PostVote(final Long id, 
                    final Post post, 
                    final User user, 
                    final Integer upvotes,
                    final Integer downvotes,
                    final Timestamp createdAt, 
                    final Timestamp updatedAt) {
        this.id = id;
        this.post = post;
        this.user = user;
        this.upvotes = 0;
        this.downvotes = 0;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
