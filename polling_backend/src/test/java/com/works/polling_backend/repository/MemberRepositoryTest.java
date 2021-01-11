package com.works.polling_backend.repository;

import com.works.polling_backend.domain.Member;
import com.works.polling_backend.domain.Survey;
import com.works.polling_backend.domain.Vote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    void save() {
        //given
        Member member = new Member();

        List<Survey> surveyList = new ArrayList<>();

        Vote vote = new Vote();
        List<Vote> voteList = new ArrayList<>();
        voteList.add(vote);

        member.setUserName("ssssss");
        member.setPassWord("aaaaa");
        member.setSurveys(surveyList);
        member.setVotes(voteList);

        //when
        memberRepository.save(member);

        //then
        assertEquals(member,memberRepository.findOne(member.getId()));
        List<Survey> res = memberRepository.findOne(member.getId()).getSurveys();
        System.out.println("res:"+res);
    }
}