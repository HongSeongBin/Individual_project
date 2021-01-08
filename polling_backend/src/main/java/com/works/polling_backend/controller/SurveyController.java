package com.works.polling_backend.controller;


import com.works.polling_backend.domain.Survey;
import com.works.polling_backend.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/api")
public class SurveyController {
    private final SurveyService surveyService;

    //설문전체목록조회
    @GetMapping("/survey")
    public List<SurveyData> getAllPolling(){
        List<Survey> res = surveyService.findSurveys();
        List<SurveyData> response = new ArrayList<>();

        for(Survey s : res)
            response.add(new SurveyData(s.getMember().getUserName(), s.getTitle(), s.getStartDate(), s.getId()));

        return response;
    }

    //내가만든설문조회
    @PostMapping("/myMaking")
    public List<SurveyData> getMadePolling(@RequestBody MemberData member){
        List<Survey> res = surveyService.findSurveyByMake(member.getId());
        List<SurveyData> response = new ArrayList<>();

        for(Survey s : res)
            response.add(new SurveyData(s.getMember().getUserName(), s.getTitle(), s.getStartDate(), s.getId()));

        return response;
    }
}

@Value
class SurveyData{
    String madeBy;
    String title;
    LocalDateTime startDate;
    Long id;

    SurveyData(String madeBy, String title, LocalDateTime startDate,Long id){
        this.madeBy = madeBy;
        this.title = title;
        this.startDate = startDate;
        this.id = id;
    }
}