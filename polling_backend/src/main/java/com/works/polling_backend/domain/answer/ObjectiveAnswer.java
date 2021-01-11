package com.works.polling_backend.domain.answer;

import com.works.polling_backend.domain.Question;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
public class ObjectiveAnswer {

    @Id @GeneratedValue
    @Column(name = "objective_answer_id")
    private Long id;

    private String answer;

    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    // 설문조사 생성 메서드
    public static ObjectiveAnswer createObjectiveAnswer(String answer){
        ObjectiveAnswer objectiveAnswer = new ObjectiveAnswer();
        objectiveAnswer.setAnswer(answer);
        objectiveAnswer.setCount(0);

        return objectiveAnswer;
    }
}
