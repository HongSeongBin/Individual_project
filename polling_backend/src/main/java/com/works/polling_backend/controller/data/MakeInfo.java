package com.works.polling_backend.controller.data;

import com.works.polling_backend.controller.data.QuestionInfo;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
public class MakeInfo {
    Long userId;
    String title;
    List<QuestionInfo> questions = new ArrayList<>();

    public MakeInfo(Long userId, String title) {
        this.userId = userId;
        this.title = title;
    }
}
