package com.works.polling_backend.controller.data;

import lombok.Value;

@Value
public class MemberData {
    Long id;
    String userName;
    String passWord;

    public MemberData(Long id, String userName, String passWord) {
        this.id = id;
        this.userName = userName;
        this.passWord = passWord;
    }
}
