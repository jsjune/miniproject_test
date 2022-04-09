package com.one.miniproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.one.miniproject.dto.ResponseDto;
import com.one.miniproject.dto.SignUpRequestDto;
import com.one.miniproject.service.KakaoUserService;
import com.one.miniproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;
    private  final KakaoUserService kakaoUserService;

    @Autowired
    public UserController(UserService userService, KakaoUserService kakaoUserService) {
        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
    }

    // 로그인 요청
//    @PostMapping("/user/login")
//    public ResponseDto login(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return userService.login(userDetails);
//    }

    // 회원가입
    @PostMapping("/user/signup")
    public ResponseDto signup(@RequestBody SignUpRequestDto requestDto) {
        return userService.registerUser(requestDto);
    }

    // 닉네임 중복 검사
//    @PostMapping("/user/nicknameCheck")
//    public ResponseDto nicknameCheck(@RequestBody SignUpRequestDto requestDto) {
//
//        return ;
//    }

    // 카카오 로그인 인증 코드 전달
    @GetMapping("/user/kakao/callback")
    public ResponseDto kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드
        kakaoUserService.kakaoLogin(code);
        return
    }
}

// https://kauth.kakao.com/oauth/authorize?client_id=5f5b48d19b29e4d68b70219642d9c40d&redirect_uri=http://localhost:8080/user/kakao/callback&response_type=code
