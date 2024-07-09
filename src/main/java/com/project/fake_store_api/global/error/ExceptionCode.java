package com.project.fake_store_api.global.error;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    ACCESS_DENIED(401,"접근 권한이 없습니다."),
    INVALID_TOKEN(401,"인가되지 않은 토큰입니다."),
    UNAUTHORIZED(401,"인증 정보가 없습니다."),
    TOKEN_NOT_FOUND(401, "Refresh 토큰이 없습니다."),
    EXPIRED_TOKEN(401,"인증이 만료되었습니다."),
    EXPIRED_REFRESH_TOKEN(401,"Refresh 토큰이 만료되었습니다."),
    USER_NOT_FOUND(404, "해당 아이디가 존재하지 않습니다."),
    USER_DUPLICATE(404, "해당 아이디가 이미 존재합니다."),
    PASSWORD_MISMATCH(404, "비밀번호가 일치하지 않습니다."),
    INVALID_TYPE(400, "유효하지 않은 형식입니다."),
    CART_NOT_FOUND(404, "해당 카트가 존재하지 않습니다."),
    ITEM_NOT_FOUND(404, "해당 아이템이 존재하지 않습니다.");

    private int status;

    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}