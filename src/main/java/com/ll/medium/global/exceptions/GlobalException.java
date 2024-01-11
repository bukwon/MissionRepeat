package com.ll.medium.global.exceptions;

import com.ll.medium.global.rsData.RsData.RsData;
import com.ll.medium.standard.base.Empty;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
    private final RsData<Empty> rsData; // Empty 없다 제너릭, 굳이 void 안 쓰고 empty 쓴 이유는 나중에 typescript 문서화 때문에

    public GlobalException(String resultCode, String msg) {
        super("resultCode=" + resultCode + ",msg=" + msg);
        this.rsData = RsData.of(resultCode, msg);
    }
}