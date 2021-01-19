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
import java.util.Map;

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

        //질문 저장
        for(QuestionInfo questionInfo : makeInfo.getQuestions()){
            Question tempQes = new Question();

            //대답리스트 존재x -> 주관식
            if(questionInfo.getAnswers().size() == 0){
                tempQes.setType(QuestionStatus.SUBJECTIVE);
                tempQes.setQuestion(questionInfo.getQuestion());

                qes.add(tempQes);
            }
            else if(questionInfo.getAnswers().size() > 0){
                tempQes.setType(QuestionStatus.OBJECTIVE);
                tempQes.setQuestion(questionInfo.getQuestion());

                List<ObjectiveAnswer> objectiveAnswers = new ArrayList<>();

                for (AnswerInfo answer : questionInfo.getAnswers()) {
                    objectiveAnswers.add(ObjectiveAnswer.createObjectiveAnswer(answer.getAnswer()));
                }

                tempQes.setObjAnswers(objectiveAnswers);
                qes.add(tempQes);
            }
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
            checkVote = "yes";
        else
            checkVote = "no";

        //설문투표여부와 해당 설문 정보를 바탕으로 response data 생성
        VoteResponse voteResponse = new VoteResponse(checkVote,surveyInfo);

        //response data에 객관식,주관식답변에 대한 정보 추가
        for(Question q : survey.getQuestion()){
            if(q.getType() == QuestionStatus.OBJECTIVE){
                QuestionInfo tempObj = new QuestionInfo(q.getId(),"objective",q.getQuestion());
                int total_count=0;

                for(ObjectiveAnswer objectiveAnswer : q.getObjAnswers())
                    total_count += objectiveAnswer.getCount();

                for(ObjectiveAnswer objectiveAnswer : q.getObjAnswers())
                    tempObj.getAnswers().add(new AnswerInfo(objectiveAnswer.getId(), objectiveAnswer.getAnswer(),objectiveAnswer.getCount(),total_count));


                voteResponse.getQuestions().add(tempObj);
            }
            else if(q.getType() == QuestionStatus.SUBJECTIVE){
                QuestionInfo tempSbj = new QuestionInfo(q.getId(),"subjective",q.getQuestion());
                for(SubjectiveAnswer answer : q.getSbjAnswers()){
                    tempSbj.getAnswers().add(new AnswerInfo(answer.getId(),answer.getAnswer(),0,0));
                }
                voteResponse.getQuestions().add(tempSbj);
            }
        }

        return voteResponse;
    }

    //대답 반영하기
    @PostMapping("/answerSurvey")
    public ResponseEntity updateAnswer(@RequestBody UpdateData updateData){

        updateData.getAnswers().forEach((questionId,answer)->{
            surveyService.saveAnswer(questionId,answer);
        });
        surveyService.updateVoteInfo(updateData.getMemberId(),updateData.getSurveyId());

        return new ResponseEntity(HttpStatus.OK);
    }
}

@Value
class UpdateData{
    Map<Long,String> answers;
    Long memberId;
    Long surveyId;
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
    List<QuestionInfo> questions = new ArrayList<>();

    public VoteResponse(String isVote,SurveyData surveyInfo) {
        this.isVote = isVote;
        this.surveyInfo = surveyInfo;
    }
}

@Value
class MakeInfo{
    Long userId;
    String title;
    List<QuestionInfo> questions = new ArrayList<>();

    public MakeInfo(Long userId, String title) {
        this.userId = userId;
        this.title = title;
    }
}

@Value
class QuestionInfo{
    Long questionId;
    String question;
    String qtype;
    int total_count=0;
    List<AnswerInfo> answers = new ArrayList<>();

    public QuestionInfo(Long questionId, String qtype, String question) {
        this.questionId = questionId;
        this.qtype = qtype;
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