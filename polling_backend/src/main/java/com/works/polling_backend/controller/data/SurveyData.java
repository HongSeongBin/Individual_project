package com.works.polling_backend.controller.data;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class SurveyData {
    String madeBy;
    String title;
    LocalDateTime startDate;
    Long id;

    public SurveyData(String madeBy, String title, LocalDateTime startDate, Long id) {
        this.madeBy = madeBy;
        this.title = title;
        this.startDate = startDate;
        this.id = id;
    }
}
