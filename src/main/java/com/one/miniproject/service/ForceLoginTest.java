//package com.one.miniproject.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.one.miniproject.dto.KakaoUserInfoDto;
//import com.one.miniproject.dto.ResponseDto;
//import com.one.miniproject.dto.SignUpRequestDto;
//import com.one.miniproject.model.User;
//import com.one.miniproject.repository.UserRepository;
//import com.one.miniproject.security.UserDetailsImpl;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.UUID;
//
//@Service
//public class ForceLoginTest {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public ForceLoginTest(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public ResponseDto login(SignUpRequestDto signUpRequestDto) {
//        Boolean result;
//
//        try {
//            User user = userRepository.findByUsername(signUpRequestDto.getUsername()).orElse(null);
//            UserDetails userDetails = new UserDetailsImpl(user);
//            System.out.println(userDetails.getUsername());
//
//            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            result = true;
//            return new ResponseDto(result);
//        } catch (Exception e) {
//            result = false;
//            return new ResponseDto(result);
//        }
//
//
//    }
////        // 3. 필요시에 회원가입
////        User user = registerKakaoUserIfNeeded(signUpRequestDto);
////        // 4. 강제 로그인 처리
////        forceLogin(user);
//
//
////    // 3.
////    private User registerKakaoUserIfNeeded(SignUpRequestDto signUpRequestDto) {
////        // DB 에 중복된 Kakao Id 가 있는지 확인
////        String kakaoUsername = signUpRequestDto.getUsername();
////        User kakaoUser = userRepository.findByUsername(kakaoUsername).orElse(null);
////        if (kakaoUser == null) {
////            // 회원가입
////            // username: kakao nickname
////            String nickname = signUpRequestDto.getNickname();
////
////            // password: random UUID
////            String password = UUID.randomUUID().toString();
////            String encodedPassword = passwordEncoder.encode(password);
////
////            kakaoUser = new User(nickname, encodedPassword, kakaoUsername);
////            userRepository.save(kakaoUser);
////        }
////        return kakaoUser;
////    }
////
////    // 4.
////    private void forceLogin(User kakaoUser) {
////        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
////        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
////        SecurityContextHolder.getContext().setAuthentication(authentication);
////    }
//}
