package com.works.polling_backend.service;


import com.works.polling_backend.domain.Member;
import com.works.polling_backend.domain.Survey;
import com.works.polling_backend.domain.Vote;
import com.works.polling_backend.repository.MemberRepository;
import com.works.polling_backend.repository.SurveyRepository;
import com.works.polling_backend.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;
    private final VoteRepository voteRepository;


    //설문목록 전체 조회
    public List<Survey> findSurveys(){
        return surveyRepository.findAll();
    }

    //자신이 생성한 설문목록 조회
    public List<Survey> findSurveyByMake(Long memberId){
        return surveyRepository.findByMember(memberId);
    }

    //자신이 투표한 설문목록 조회
    public List<Survey> findSurveyByVote(Long memberId){
        Member member = memberRepository.findOne(memberId);
        List<Vote> votes = voteRepository.findByMember(member);

        List<Survey> results = new ArrayList<>();

        for(Vote v : votes)
            results.add(v.getSurvey());

        return results;
    }
}
