package com.raven.api.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cover")
@Getter
@Setter
public class Cover {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;    

    @Column(nullable = false, name = "url")
    private String url;

    @Column(nullable = false, name = "blurhash")
    private String blurHash;

    @Column(nullable = false, name = "width")
    private int width;

    @Column(nullable = false, name = "height")
    private int height;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Cover() {
    }

    public Cover(final Long id, 
                 final String url, 
                 final String blurhash, 
                 final int width,
                 final int height,
                 final Post post, 
                 final Timestamp createdAt, 
                 final Timestamp updatedAt) {
        this.id = id;
        this.url = url;
        this.blurHash = blurhash;
        this.width = width;
        this.height = height;
        this.post = post;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", url='" + getUrl() + "'" +
            ", blurhash='" + getBlurHash() + "'" +
            ", post='" + getPost() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }

}
