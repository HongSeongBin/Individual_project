package com.works.polling_backend.service;


import com.works.polling_backend.domain.*;
import com.works.polling_backend.domain.answer.ObjectiveAnswer;
import com.works.polling_backend.domain.answer.SubjectiveAnswer;
import com.works.polling_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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
    private final SubjectiveAnswerRepository subjectiveAnswerRepository;

    //설문목록 전체 조회
    public List<Survey> findSurveys(){
        return surveyRepository.findAll();
    }

    //특정 설문 조회
    public Survey findOne(Long surveyId){
        return surveyRepository.findOne(surveyId);
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

    //설문생성시 타이틀 중복 조회 체크
    public boolean validateDuplicateSurvey(String surveyName,Member member){
        if(surveyRepository.findOneByTitleMember(surveyName,member) == null)
            return true;
        return false;
    }

    //투표한적이 있는 설문인지
    public boolean checkVote(Long memberId,Long surveyId){
        Member member = memberRepository.findOne(memberId);
        Survey survey = surveyRepository.findOne(surveyId);

        if(voteRepository.checkVoting(member, survey) == null)
            return false;
        return true;
    }

    //투표정보 업데이트
    @Transactional
    public void updateVoteInfo(Long memberId, Long surveyId){
        Member member = memberRepository.findOne(memberId);
        Survey survey = surveyRepository.findOne(surveyId);

        Vote vote = new Vote();
        vote.setMember(member);
        vote.setSurvey(survey);

        voteRepository.save(vote);
    }

    //설문대답정보 반영
    @Transactional
    public Long saveAnswer(Long questionId,String answer){
        Question question = questionRepository.findOne(questionId);
        if(question.getType() == QuestionStatus.OBJECTIVE){
            ObjectiveAnswer objectiveAnswer = objectiveAnswerRepository.findByQNA(question,answer);
            objectiveAnswer.setCount(objectiveAnswer.getCount()+1);

            objectiveAnswerRepository.save(objectiveAnswer);

        } else if (question.getType() == QuestionStatus.SUBJECTIVE){
            SubjectiveAnswer subjectiveAnswer = new SubjectiveAnswer();
            subjectiveAnswer.setQuestion(question);
            subjectiveAnswer.setAnswer(answer);

            subjectiveAnswerRepository.save(subjectiveAnswer);
        }

        return question.getSurvey().getId();
    }

}
