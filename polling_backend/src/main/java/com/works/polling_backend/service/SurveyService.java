package com.works.polling_backend.service;


import com.works.polling_backend.domain.Survey;
import com.works.polling_backend.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;

    //설문목록 전체 조회
    public List<Survey> findSurveys(){
        return surveyRepository.findAll();
    }
}
