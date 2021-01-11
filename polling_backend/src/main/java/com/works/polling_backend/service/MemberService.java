package com.works.polling_backend.service;

import com.works.polling_backend.domain.Member;
import com.works.polling_backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
        Member res=null;

        List<Member> findMembers = memberRepository.findByName(userName);

        //존재하지 않을 경우 null
        if(findMembers.isEmpty())
            return res;
        for(Member m : findMembers){
            if(m.getPassWord().equals(password)){
                res = m;
                break;
            }
        }
        return res;
    }

    //Register
    @Transactional
    public Long join(Member member){
        memberRepository.save(member);
        return member.getId();
    }

    //특정 member 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }


}
