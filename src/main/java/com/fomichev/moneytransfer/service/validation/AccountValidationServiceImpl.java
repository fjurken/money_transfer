package com.fomichev.moneytransfer.service.validation;

import com.fomichev.moneytransfer.EntityStatus;
import com.fomichev.moneytransfer.exception.AccountHasNoEnoughMoneyException;
import com.fomichev.moneytransfer.exception.AccountStatusException;
import com.fomichev.moneytransfer.exception.AccountValidationException;
import com.fomichev.moneytransfer.exception.BaseException;
import com.fomichev.moneytransfer.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Account validation service
 */
@Service
public class AccountValidationServiceImpl implements AccountValidationService {

    /**
     * Validate account FROM
     * @param account - Account to validate
     * @param requiredSum - sum (required amount)
     */
    @Override
    public void validateAccountFrom(Account account, Double requiredSum) throws AccountValidationException {
        try {
            isAccountActive(account);
            isAccountHasEnoughMoney(account, requiredSum);
        } catch (BaseException e) {
            throw new AccountValidationException(e.getMessage(), e);
        }
    }

    /**
     * Validate account TO
     * @param account - Account to validate
     */
    @Override
    public void validateAccountTo(Account account) throws AccountValidationException {
        try {
            isAccountActive(account);
        } catch (BaseException e) {
            throw new AccountValidationException(e.getMessage(), e);
        }
    }

    /**
     * Validate Account is active by its status
     * @param account - Account to validate
     */
    @Override
    public void isAccountActive(Account account) throws AccountStatusException {
        if (account.getStatus() != EntityStatus.ACTIVE) {
            throw new AccountStatusException("Account " + account.getId() + " status is not Active!", null);
        }
    }

    /**
     * Validate Account to contains enough money
     * @param account - Account to validate
     * @param requiredSum - sum (required amount)
     */
    @Override
    public void isAccountHasEnoughMoney(Account account, Double requiredSum) throws AccountHasNoEnoughMoneyException {
        if (account.getBalance().compareTo(BigDecimal.valueOf(requiredSum)) < 0) {
            throw new AccountHasNoEnoughMoneyException("Account " + account.getId() + " has no enough money", null);
        }
    }
}
