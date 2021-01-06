package com.works.polling_backend.controller;

import com.works.polling_backend.domain.Member;
import com.works.polling_backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    //로그인 요청시 응답
    @PostMapping("login")
    public ResponseEntity login(@RequestBody MemberResponse member) {
        Member res = memberService.login(member.getUserName(), member.getPassWord());
        if (res == null)
            return new ResponseEntity(-1, HttpStatus.ACCEPTED);

        return new ResponseEntity(new MemberResponse(res.getId(), res.getUserName(), res.getPassWord()), HttpStatus.ACCEPTED);
    }
}

@Value
class MemberResponse {
    Long id;
    String userName;
    String passWord;

    MemberResponse(Long id, String userName, String passWord) {
        this.id = id;
        this.userName = userName;
        this.passWord = passWord;
    }
}