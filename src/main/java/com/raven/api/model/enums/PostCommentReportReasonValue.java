package com.raven.api.model.enums;

import lombok.Getter;

@Getter
public enum PostCommentReportReasonValue {

    REASON_ONE("Suggestive, unsettling, contains mature themes"),
    REASON_TWO("Nudity, sexual activity, non-consensual sexualization"),
    REASON_FOUR("Spam, fake news, scams, fraud"),
    REASON_THREE("Discrimination, hateful ideology, illegal content, gore"),
    REASON_FIVE("Harassment, self-harm, personal information");

    private final String value;

    PostCommentReportReasonValue(String value) {
        this.value = value;
    }
    
}
