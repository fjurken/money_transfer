package com.fomichev.moneytransfer.service.validation;

import com.fomichev.moneytransfer.EntityStatus;
import com.fomichev.moneytransfer.exception.AccountHasNoEnoughMoneyException;
import com.fomichev.moneytransfer.exception.AccountStatusException;
import com.fomichev.moneytransfer.exception.AccountValidationException;
import com.fomichev.moneytransfer.exception.BaseException;
import com.fomichev.moneytransfer.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountValidationServiceImpl implements AccountValidationService {

    @Override
    public void validateAccountFrom(Account account, Double requiredSum) throws AccountValidationException {
        try {
            isAccountActive(account);
            isAccountHasEnoughMoney(account, requiredSum);
        } catch (BaseException e) {
            throw new AccountValidationException(e.getMessage(), e);
        }
    }

    @Override
    public void validateAccountTo(Account account) throws AccountValidationException {
        try {
            isAccountActive(account);
        } catch (BaseException e) {
            throw new AccountValidationException(e.getMessage(), e);
        }
    }

    @Override
    public void isAccountActive(Account account) throws AccountStatusException {
        if (account.getStatus() != EntityStatus.ACTIVE) {
            throw new AccountStatusException("Account " + account.getId() + " status is not Active!", null);
        }
    }

    @Override
    public void isAccountHasEnoughMoney(Account account, Double requiredSum) throws AccountHasNoEnoughMoneyException {
        if (account.getBalance().compareTo(BigDecimal.valueOf(requiredSum)) < 0) {
            throw new AccountHasNoEnoughMoneyException("Account " + account.getId() + " has no enough money", null);
        }
    }
}
