package com.works.polling_backend.controller.data;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
public class QuestionInfo {
    Long questionId;
    String question;
    String qtype;
    int total_count = 0;
    List<AnswerInfo> answers = new ArrayList<>();

    public QuestionInfo(Long questionId, String qtype, String question) {
        this.questionId = questionId;
        this.qtype = qtype;
        this.question = question;
    }

}
