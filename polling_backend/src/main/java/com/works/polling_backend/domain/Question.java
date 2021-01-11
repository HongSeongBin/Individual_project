package com.works.polling_backend.domain;

import com.works.polling_backend.domain.answer.ObjectiveAnswer;
import com.works.polling_backend.domain.answer.SubjectiveAnswer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Question {

    @Id @GeneratedValue
    @Column(name = "question_id")
    private Long id;

    private String question;

    @Enumerated(EnumType.STRING)
    private QuestionStatus type; // 설문조사 형태 (주관식, 객관식)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "question")
    private List<SubjectiveAnswer> sbjAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "question")
    private List<ObjectiveAnswer> objAnswers = new ArrayList<>();

    public void addSbjAnswer(SubjectiveAnswer subjectiveAnswer){
        sbjAnswers.add(subjectiveAnswer);
        subjectiveAnswer.setQuestion(this);
    }

    public void addObjAnswer(ObjectiveAnswer objectiveAnswer){
        objAnswers.add(objectiveAnswer);
        objectiveAnswer.setQuestion(this);
    }

}
