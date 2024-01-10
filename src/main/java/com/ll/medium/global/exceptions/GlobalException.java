package com.ll.medium.global.exceptions;

import com.ll.medium.global.rsData.RsData.RsData;

public class GlobalException extends RuntimeException {
    private RsData<?> rsData;

    public GlobalException(String resultCode, String msg) {
        super(msg);
        this.rsData = RsData.of(resultCode, msg);
    }
}