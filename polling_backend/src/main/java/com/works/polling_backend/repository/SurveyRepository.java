package com.works.polling_backend.repository;

import com.works.polling_backend.domain.Member;
import com.works.polling_backend.domain.Question;
import com.works.polling_backend.domain.Survey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SurveyRepository {
    private final EntityManager em;

    public void save(Survey survey){
        em.persist(survey);
    }

    //회원바탕 자신이 만든 설문조사 조회
    public List<Survey> findByMember(Long id){
        return em.createQuery("select s from Survey s where s.member.id = :id",Survey.class)
                .setParameter("id",id)
                .getResultList();
    }

    //설문조사 타이틀,사용자 바탕으로 조회
    public Survey findOneByTitleMember(String title,Member member){
        return em.createQuery("select s from Survey s where s.title=:title and s.member=:member",Survey.class)
                .setParameter("title",title)
                .setParameter("member",member)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    //설문조사 하나 조회
    public Survey findOne(Long id){
        return em.find(Survey.class, id);
    }

    //전체 설문조사 조회
    public List<Survey> findAll(){
        return em.createQuery("select s from Survey s",Survey.class).getResultList();
    }
}
