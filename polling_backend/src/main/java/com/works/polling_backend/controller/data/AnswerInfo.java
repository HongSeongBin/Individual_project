package com.works.polling_backend.controller.data;

import lombok.Value;

@Value
public class AnswerInfo {
    Long answerId;
    String answer;
    int cnt;
    int total;

    public AnswerInfo(Long answerId, String answer, int cnt, int total) {
        this.answerId = answerId;
        this.answer = answer;
        this.cnt = cnt;
        this.total = total;
    }
}
