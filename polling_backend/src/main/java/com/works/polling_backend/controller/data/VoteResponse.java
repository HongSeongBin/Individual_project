package com.works.polling_backend.controller.data;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
public class VoteResponse {
    String isVote;
    SurveyData surveyInfo;
    List<QuestionInfo> questions = new ArrayList<>();

    public VoteResponse(String isVote, SurveyData surveyInfo) {
        this.isVote = isVote;
        this.surveyInfo = surveyInfo;
    }
}
