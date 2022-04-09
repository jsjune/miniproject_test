package com.one.miniproject.service;

import com.one.miniproject.dto.ResponseDto;
import com.one.miniproject.dto.SignUpRequestDto;
import com.one.miniproject.model.User;
import com.one.miniproject.repository.UserRepository;
import com.one.miniproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
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
            throw new IllegalArgumentException("중복된 ID 존재");
        }

        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(username, password, nickname);
        userRepository.save(user);

        ResponseDto responseDto = new ResponseDto(result,err_msg);
        return responseDto;

    }

//    public ResponseDto login(UserDetailsImpl userDetails) {
//        String nickname = userDetails.getNickname();
//        Boolean result = false;
//
//        if (userDetails.getUsername() != null) {
//            result = true;
//        }
//    }
}
