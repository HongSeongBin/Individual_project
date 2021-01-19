package com.works.polling_backend.repository;

import com.works.polling_backend.domain.Member;
import com.works.polling_backend.domain.Question;
import com.works.polling_backend.domain.Survey;
import com.works.polling_backend.domain.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VoteRepository {

    private final EntityManager em;

    //vote 관계 테이블 저장
    public void save(Vote vote){
        em.persist(vote);
    }

    //특정 회원을 바탕으로 투표한 vote 정보 반환
    public List<Vote> findByMember(Member member){
        return em.createQuery("select v from Vote v where v.member = :member ",Vote.class)
                .setParameter("member",member)
                .getResultList();
    }

    //특정 회원정보와 설문정보로 투표한 유무 반환
    public Vote checkVoting(Member member, Survey survey){
        return em.createQuery("select v from Vote v where v.member = :member and v.survey = :survey",Vote.class)
                .setParameter("member",member)
                .setParameter("survey",survey)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }
}
