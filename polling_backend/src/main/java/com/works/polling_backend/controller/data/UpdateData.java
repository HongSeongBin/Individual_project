package com.works.polling_backend.controller.data;

import lombok.Value;

import java.util.Map;

@Value
public class UpdateData {
    Map<Long, String> answers;
    Long memberId;
    Long surveyId;
}
