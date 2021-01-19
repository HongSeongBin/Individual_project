package com.works.polling_backend.repository;

import com.works.polling_backend.domain.Member;
import com.works.polling_backend.domain.Question;
import com.works.polling_backend.domain.Vote;
import com.works.polling_backend.domain.answer.ObjectiveAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ObjectiveAnswerRepository {

    private final EntityManager em;

    public void save(ObjectiveAnswer objectiveAnswer){
        if(objectiveAnswer.getId() == null) {
            em.persist(objectiveAnswer);
        }else{
            em.merge(objectiveAnswer);
        }
    }

    //질문과 해답을 바탕으로 대답조회
    public ObjectiveAnswer findByQNA(Question question,String answer){
        return em.createQuery("select o from ObjectiveAnswer o where o.question = :question and o.answer=:answer",ObjectiveAnswer.class)
                .setParameter("question",question)
                .setParameter("answer",answer)
                .getSingleResult();
    }

    //특정 질문에 대한 대답들 반환
    public List<ObjectiveAnswer> findByQuestion(Question question){
        return em.createQuery("select o from ObjectiveAnswer o where o.question = :question ",ObjectiveAnswer.class)
                .setParameter("question",question)
                .getResultList();
    }
}
