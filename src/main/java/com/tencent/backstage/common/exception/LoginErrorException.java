package com.tencent.backstage.common.exception;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/22
 * Time:17:30
 */
public class LoginErrorException extends RuntimeException{

    public LoginErrorException(String errorMsg){
        super(LoginErrorException.returnErrorMsg(errorMsg));
    }

    public static  String returnErrorMsg(String errorMsg){
        return errorMsg;
    }

}
