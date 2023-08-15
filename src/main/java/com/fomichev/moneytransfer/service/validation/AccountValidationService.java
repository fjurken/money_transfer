package com.fomichev.moneytransfer.service.validation;

import com.fomichev.moneytransfer.exception.AccountHasNoEnoughMoneyException;
import com.fomichev.moneytransfer.exception.AccountStatusException;
import com.fomichev.moneytransfer.exception.AccountValidationException;
import com.fomichev.moneytransfer.model.Account;

public interface AccountValidationService {

    void validateAccountFrom(Account account, Double requiredSum) throws AccountValidationException;

    void validateAccountTo(Account account) throws AccountValidationException;

    void isAccountActive(Account account) throws AccountStatusException;

    void isAccountHasEnoughMoney(Account account, Double requiredSum) throws AccountHasNoEnoughMoneyException;

}
