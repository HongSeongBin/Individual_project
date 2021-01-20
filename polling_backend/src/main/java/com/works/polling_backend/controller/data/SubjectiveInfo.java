package com.works.polling_backend.controller.data;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
public class SubjectiveInfo {
    Long questionId;
    String question;
    List<String> answer = new ArrayList<>();

    public SubjectiveInfo(Long questionId, String question) {
        this.questionId = questionId;
        this.question = question;
    }
}
