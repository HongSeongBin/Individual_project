package com.works.polling_backend.controller;


import com.works.polling_backend.domain.Member;
import com.works.polling_backend.domain.Question;
import com.works.polling_backend.domain.QuestionStatus;
import com.works.polling_backend.domain.Survey;
import com.works.polling_backend.domain.answer.ObjectiveAnswer;
import com.works.polling_backend.service.MemberService;
import com.works.polling_backend.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final MemberService memberService;

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

    //내가 투표한 설문
    @PostMapping("/myVoting")
    public List<SurveyData> getVotePolling(@RequestBody MemberData member){
        List<Survey> res = surveyService.findSurveyByVote(member.getId());
        List<SurveyData> response = new ArrayList<>();

        for(Survey s : res)
            response.add(new SurveyData(s.getMember().getUserName(), s.getTitle(), s.getStartDate(), s.getId()));

        return response;
    }

    //설문생성
    @PostMapping("/makeSurvey")
    public ResponseEntity makePolling(@RequestBody MakeInfo makeInfo){
        Member madeBy = memberService.findOne(makeInfo.getUserId());
        String title = makeInfo.getTitle();
        List<Question> qes = new ArrayList<>();

        //객관식 설문정보 저장
        for(ObjectiveInfo objectiveInfo : makeInfo.getObjectives()){
            Question tempQes = new Question();
            tempQes.setType(QuestionStatus.OBJECTIVE);
            tempQes.setQuestion(objectiveInfo.getQuestion());

            List<ObjectiveAnswer> objectiveAnswers = new ArrayList<>();

            for(AnswerInfo answer : objectiveInfo.getAnswers()){
                objectiveAnswers.add(ObjectiveAnswer.createObjectiveAnswer(answer.getAnswer()));
            }

            tempQes.setObjAnswers(objectiveAnswers);
            qes.add(tempQes);
        }

        //주관식 설문정보 저장
        for(SubjectiveInfo subjectiveInfo : makeInfo.getSubjectives()){
            Question tempQes = new Question();
            tempQes.setType(QuestionStatus.SUBJECTIVE);
            tempQes.setQuestion(subjectiveInfo.getQuestion());

            qes.add(tempQes);
        }

        surveyService.makeSurvey(madeBy.getId(),title,qes);

        return new ResponseEntity(HttpStatus.ACCEPTED);

    }
}

@Value
class MakeInfo{
    Long userId;
    String title;
    List<ObjectiveInfo> objectives = new ArrayList<>();
    List<SubjectiveInfo> subjectives = new ArrayList<>();

    public MakeInfo(Long userId, String title) {
        this.userId = userId;
        this.title = title;
    }
}

@Value
class ObjectiveInfo{
    Long questionId;
    String question;
    int total_count=0;
    List<AnswerInfo> answers = new ArrayList<>();

    public ObjectiveInfo(Long questionId, String question) {
        this.questionId = questionId;
        this.question = question;
    }

}

@Value
class AnswerInfo{
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

@Value
class SubjectiveInfo{
    Long questionId;
    String question;
    List<String> answer = new ArrayList<>();

    public SubjectiveInfo(Long questionId, String question) {
        this.questionId = questionId;
        this.question = question;
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