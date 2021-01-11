package com.works.polling_backend.service;


import com.works.polling_backend.domain.*;
import com.works.polling_backend.domain.answer.ObjectiveAnswer;
import com.works.polling_backend.repository.*;
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
    private final QuestionRepository questionRepository;
    private final ObjectiveAnswerRepository objectiveAnswerRepository;

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

    //객관식 대답 저장
    @Transactional
    public void saveObjectiveAnswer(List<ObjectiveAnswer> answer, Long questionId){
        Question question = questionRepository.findOne(questionId);

        for(ObjectiveAnswer s : answer) {
            s.setQuestion(question);

            objectiveAnswerRepository.save(s);
        }
    }

    //설문 생성
    @Transactional
    public Long makeSurvey(Long memberId, String title, List<Question> questionList){
        Member member = memberRepository.findOne(memberId);
        Survey survey = Survey.createSurvey(member,title);

        surveyRepository.save(survey);

        for(Question q : questionList){
            q.setSurvey(survey);
            questionRepository.save(q);

            if(q.getType() == QuestionStatus.OBJECTIVE){
                saveObjectiveAnswer(q.getObjAnswers(),q.getId());
            }
        }

        return survey.getId();
    }

    //설문타이틀 바탕 설문조회
    public Survey findSurveyByTitle(String surveyName){
        return surveyRepository.findOneByTitle(surveyName);
    }

    //투표한적이 있는 설문인지
    public Vote checkVote(Long memberId,String surveyName){
        Member member = memberRepository.findOne(memberId);
        Survey survey = surveyRepository.findOneByTitle(surveyName);

        try {
            Vote voteInfo = voteRepository.checkVoting(member, survey);
            return voteInfo;
        }catch(Exception e){
            return null;
        }
    }

}
