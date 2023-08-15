package com.fomichev.moneytransfer.exception;

public class AccountHasNoEnoughMoneyException extends BaseException {

    public AccountHasNoEnoughMoneyException(String message, Throwable cause) {
        super(message, cause);
    }

}
