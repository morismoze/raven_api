package com.raven.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.raven.api.model.enums.TagName;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tag")
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private TagName tagName;

    public Tag() {
    }

    public Tag(final Long id, 
               final TagName tagName) {
        this.id = id;
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", tagName='" + getTagName() + "'" +
            "}";
    }

}

