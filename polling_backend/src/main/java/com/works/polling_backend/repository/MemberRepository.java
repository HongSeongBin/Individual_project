package com.works.polling_backend.repository;

import com.works.polling_backend.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    //id 기반 찾기
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    //이름 기반 찾기
    public Member findByName(String userName){
        return em.createQuery("select m from Member m where m.userName = :name", Member.class)
                .setParameter("name",userName)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

}
