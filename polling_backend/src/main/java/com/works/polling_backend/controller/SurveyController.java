package com.works.polling_backend.controller;

import com.works.polling_backend.domain.*;
import com.works.polling_backend.domain.answer.ObjectiveAnswer;
import com.works.polling_backend.domain.answer.SubjectiveAnswer;
import com.works.polling_backend.service.MemberService;
import com.works.polling_backend.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        String title = makeInfo.getTitle();
        Member madeBy = memberService.findOne(makeInfo.getUserId());

        if(!surveyService.validateDuplicateSurvey(title, madeBy)){
            return new ResponseEntity(-1,HttpStatus.BAD_REQUEST);
        }

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

        return new ResponseEntity(HttpStatus.OK);

    }


    //설문 투표 정보 반환
    @GetMapping("/voteSurvey")
    public VoteResponse votePolling(@RequestParam("memberId") String memberId, @RequestParam("surveyId") String surveyId){
        Member member = memberService.findOne(Long.valueOf(memberId));
        Survey survey = surveyService.findOne(Long.valueOf(surveyId));

        SurveyData surveyInfo = new SurveyData(survey.getMember().getUserName(), survey.getTitle(), survey.getStartDate(), survey.getId());
        String checkVote;

        //설문 투표한적이 있는지 없는지
        if(surveyService.checkVote(member.getId(), survey.getId()))
            checkVote="yes";
        else
            checkVote="no";

        //설문투표여부와 해당 설문 정보를 바탕으로 response data 생성
        VoteResponse voteResponse = new VoteResponse(checkVote,surveyInfo);

        //response data에 객관식,주관식답변에 대한 정보 추가
        for(Question q : survey.getQuestion()){
            if(q.getType() == QuestionStatus.OBJECTIVE){
                ObjectiveInfo tempObj = new ObjectiveInfo(q.getId(),q.getQuestion());
                int total_count=0;

                for(ObjectiveAnswer objectiveAnswer : q.getObjAnswers())
                    total_count += objectiveAnswer.getCount();

                for(ObjectiveAnswer objectiveAnswer : q.getObjAnswers())
                    tempObj.getAnswers().add(new AnswerInfo(objectiveAnswer.getId(), objectiveAnswer.getAnswer(),objectiveAnswer.getCount(),total_count));

                voteResponse.getObjectives().add(tempObj);
            }
            else if(q.getType() == QuestionStatus.SUBJECTIVE){
                SubjectiveInfo subjectiveInfo = new SubjectiveInfo(q.getId(),q.getQuestion());
                for(SubjectiveAnswer answer : q.getSbjAnswers()){
                    subjectiveInfo.getAnswer().add(answer.getAnswer());
                }
                voteResponse.getSubjectives().add(subjectiveInfo);
            }
        }

        return voteResponse;
    }
}

@Value
class VoteInfo{
    Long memberId;
    Long surveyId;
    String surveyName;
}

@Value
class VoteResponse{
    String isVote;
    SurveyData surveyInfo;
    List<ObjectiveInfo> objectives = new ArrayList<>();
    List<SubjectiveInfo> subjectives = new ArrayList<>();

    public VoteResponse(String isVote,SurveyData surveyInfo) {
        this.isVote = isVote;
        this.surveyInfo = surveyInfo;
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