package com.one.miniproject.dto;

import lombok.Getter;

@Getter
public class KakaoUserInfoDto {
    private String username;
    private String nickname;
//    private String email;

    public KakaoUserInfoDto(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }

//    public KakaoUserInfoDto(Long id, String nickname, String email) {
//        this.id = id;
//        this.nickname = nickname;
//        this.email = email;
//    }
}
