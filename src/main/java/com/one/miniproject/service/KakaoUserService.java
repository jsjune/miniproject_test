package com.one.miniproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.one.miniproject.dto.KakaoUserInfoDto;
import com.one.miniproject.dto.ResponseDto;
import com.one.miniproject.model.User;
import com.one.miniproject.repository.UserRepository;
import com.one.miniproject.security.UserDetailsImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class KakaoUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public KakaoUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseDto kakaoLogin(String code) throws JsonProcessingException {
        Boolean result;
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        try {
            // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
            KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
            // 3. 필요시에 회원가입
            User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);
            // 4. 강제 로그인 처리
            forceLogin(kakaoUser);
            result=true;
            return new ResponseDto(kakaoUser.getNickname(), result);
        } catch (Exception e) {
            result=false;
            String nickname = null;
            return new ResponseDto(nickname, result);
        }
    }

    // 1.
    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "5f5b48d19b29e4d68b70219642d9c40d");
        body.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    // 2.
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String username = jsonNode.get("id").asText();
//        String nickname = jsonNode.get("properties").get("nickname").asText();
//        String email = jsonNode.get("kakao_account")
//                .get("email").asText();

        String nickname;
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            nickname = user.getNickname();
        } else {
            nickname = null;
        }

        return new KakaoUserInfoDto(username,nickname);

//        return new KakaoUserInfoDto(id, nickname, email);
    }

    // 3.
    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        String kakaoUsername = kakaoUserInfo.getUsername();
        User kakaoUser = userRepository.findByUsername(kakaoUsername).orElse(null);
        if (kakaoUser == null) {
            // 회원가입
            // username: kakao nickname
//            String nickname = kakaoUserInfo.getNickname();
            String nickname = null;

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            kakaoUser = new User(nickname, encodedPassword, kakaoUsername);
            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

    // 4.
    private void forceLogin(User kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
