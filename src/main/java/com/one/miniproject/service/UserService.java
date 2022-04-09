package com.one.miniproject.service;

import com.one.miniproject.dto.ResponseDto;
import com.one.miniproject.dto.SignUpRequestDto;
import com.one.miniproject.model.User;
import com.one.miniproject.repository.UserRepository;
import com.one.miniproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public ResponseDto registerUser(SignUpRequestDto requestDto) {
        Boolean result = true;
        String err_msg = "사용가능한 ID 입니다.";
        String username = requestDto.getUsername();
        String nickname = requestDto.getNickname();

        Optional<User> found = userRepository.findByUsername(username);

        if (found.isPresent()) {
            err_msg = "중복된 ID가 존재합니다.";
            result = false;
            return new ResponseDto(result, err_msg,nickname);
//            throw new IllegalArgumentException("중복된 ID 존재");
        }
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 비밀번호 복호화 실험험
//        System.out.println(password);
//        System.out.println(requestDto.getPassword());
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        if (!found.isPresent()&&encoder.matches(requestDto.getPassword(), password)) {
//            System.out.println("실험성공!");
//        }

        User user = new User(username, password, nickname);
        userRepository.save(user);

        ResponseDto responseDto = new ResponseDto(result,err_msg,nickname);
        return responseDto;

    }

    public ResponseDto login(SignUpRequestDto requestDto) {
        Boolean result = false;
        Optional<User> found = userRepository.findByUsername(requestDto.getUsername());
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (found.isPresent() && passwordEncoder.matches(requestDto.getPassword(), found.get().getPassword())) {
            result = true;
            return new ResponseDto(result);
        }
        return new ResponseDto(result);
    }


    public ResponseDto nicknameCheck(SignUpRequestDto requestDto) {
        Boolean result = true;
        if (userRepository.findByNickname(requestDto.getNickname())) {
            result = false;
            return new ResponseDto(result);
        }
        return new ResponseDto(result);
    }
}
