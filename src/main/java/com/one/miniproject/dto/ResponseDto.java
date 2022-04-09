package com.one.miniproject.dto;

import lombok.Getter;

@Getter
public class ResponseDto {
    private boolean result;
    private String err_msg;
    private String nickname;

    public ResponseDto(Boolean result, String err_msg) {
        this.result=result;
        this.err_msg=err_msg;
    }

    public ResponseDto(String username) {
        this.user
    }
}
