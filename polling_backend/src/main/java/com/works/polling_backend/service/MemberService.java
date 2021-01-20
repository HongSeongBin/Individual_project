package com.works.polling_backend.service;

import com.works.polling_backend.domain.Member;
import com.works.polling_backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //login
    public Member login(String userName, String password){
        Member res = null;

        Member findMember = memberRepository.findByName(userName);

        //존재하지 않을 경우 null
        if(findMember == null)
            return res;

        if(findMember.getPassWord().equals(password)){
            res = findMember;
        }

        return res;
    }

    //Register
    @Transactional
    public Long join(Member member){
        if(!validateDuplicateMember(member))
            return null;
        memberRepository.save(member);
        return member.getId();
    }

    //중복체크
    private boolean validateDuplicateMember(Member member){
        if(memberRepository.findByName(member.getUserName()) == null)
            return true;
        return false;
    }

    //특정 member 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }


}
