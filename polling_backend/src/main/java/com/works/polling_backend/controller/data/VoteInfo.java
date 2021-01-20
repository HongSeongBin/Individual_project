package com.works.polling_backend.controller.data;

import lombok.Value;

@Value
public class VoteInfo {
    Long memberId;
    Long surveyId;
    String surveyName;
}
