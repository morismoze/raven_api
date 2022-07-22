package com.raven.api.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.ToString;

@Entity
@Table(name = "image")
@ToString
public class Image {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "description")
    String description;    

    @Column(nullable = false, name = "path")
    String path;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "image", fetch = FetchType.EAGER)
    private List<ImageComment> imageComments;

    @OneToMany(mappedBy = "image", fetch = FetchType.EAGER)
    private List<ImageLike> imageLikes;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Image() {
    }

    public Image(final Long id, 
                 final String description, 
                 final String path, User user, 
                 final List<ImageComment> imageComments, 
                 final List<ImageLike> imageLikes, 
                 final Timestamp createdAt, 
                 final Timestamp updatedAt) {
        this.id = id;
        this.description = description;
        this.path = path;
        this.user = user;
        this.imageComments = List.copyOf(imageComments);
        this.imageLikes = List.copyOf(imageLikes);
        this.createdAt = new Timestamp(System.currentTimeMillis());;
        this.updatedAt = new Timestamp(System.currentTimeMillis());;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ImageComment> getImageComments() {
        return this.imageComments;
    }

    public void setImageComments(List<ImageComment> imageComments) {
        this.imageComments = imageComments;
    }

    public List<ImageLike> getImageLikes() {
        return this.imageLikes;
    }

    public void setImageLikes(List<ImageLike> imageLikes) {
        this.imageLikes = imageLikes;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

}
