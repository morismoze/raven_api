package com.raven.api.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_comment_report_reason")
@Getter
@Setter
public class PostCommentReportReason {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason_value")
    private String reasonValue;

    @OneToMany(mappedBy = "reason")
    private Set<PostCommentReport> postCommentReports;

    public PostCommentReportReason() {
    }

    public PostCommentReportReason(final Long id, 
                                   final String reasonValue, 
                                   final Set<PostCommentReport> postCommentReport) {
        this.id = id;
        this.reasonValue = reasonValue;
        this.postCommentReports = Set.copyOf(postCommentReport);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", reasonValue='" + getReasonValue() + "'" +
            ", postCommentReports='" + getPostCommentReports() + "'" +
            "}";
    }
   
}
