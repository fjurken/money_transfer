package com.fomichev.moneytransfer.controller.dto;

import java.io.Serializable;

public class AccountTo<A, B> implements Serializable {
    public final Long accountId;
    public final Double sum;

    public AccountTo(Long accountId, Double sum) {
        this.accountId = accountId;
        this.sum = sum;
    }
}
