package com.fomichev.moneytransfer.service.transfer;

import com.fomichev.moneytransfer.controller.dto.AccountTo;
import com.fomichev.moneytransfer.controller.dto.request.RequestTransfer;
import com.fomichev.moneytransfer.exception.AccountValidationException;
import com.fomichev.moneytransfer.model.Account;
import com.fomichev.moneytransfer.repository.TransferRepository;
import com.fomichev.moneytransfer.service.validation.AccountValidationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Money transfer service
 */
@Slf4j
@Service
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final TransferRepository transferRepository;
    private final AccountValidationService accountValidationService;

    @Autowired
    public MoneyTransferServiceImpl(
            TransferRepository transferRepository,
            AccountValidationService accountValidationService) {
        this.transferRepository = transferRepository;
        this.accountValidationService = accountValidationService;
    }

    /**
     * Transfer between accounts in single request
     * @param requestTransfer - request to transfer
     */
    @Override
    @Transactional
    public void transfer(RequestTransfer requestTransfer) throws AccountValidationException {
        try {
            /*get entities*/
            Account accountFrom = transferRepository.getReferenceById(requestTransfer.getFromAccountId());
            Account accountTo = transferRepository.getReferenceById(requestTransfer.getToAccountId());

            /*validation*/
            accountValidationService.validateAccountFrom(accountFrom, requestTransfer.getSum());
            accountValidationService.validateAccountTo(accountTo);

            /*change balances*/
            subtractMoney(accountFrom, requestTransfer.getSum());
            addMoney(accountTo, requestTransfer.getSum());

            /*save*/
            transferRepository.saveAll(List.of(accountFrom, accountTo));

            log.info(
                    "Money transferred successfully! \n" +
                            "From account id=" + accountFrom.getId()
                            + " to account id=" + accountTo.getId()
                            + " sum=" + requestTransfer.getSum());

        } catch (EntityNotFoundException e) {
            throw new AccountValidationException("Unable to find account with "
                    + e.getMessage().substring(e.getMessage().lastIndexOf("id")), e);
        }
    }

    /**
     * Transfer between account in list
     * @param transferList - list of accounts
     */
    @Override
    public void transfer(Map<Long, AccountTo<Long, Double>> transferList) throws AccountValidationException {
        try {

            for (Long id : transferList.keySet()) {

                var accountFromId = id;
                var accountToId = transferList.get(id).accountId;
                var sum = transferList.get(id).sum;

                /*get entities*/
                Account accountFrom = transferRepository.getReferenceById(accountFromId);
                Account accountTo = transferRepository.getReferenceById(accountToId);

                /*validation*/
                accountValidationService.validateAccountFrom(accountFrom, sum);
                accountValidationService.validateAccountTo(accountTo);

                /*change balances*/
                subtractMoney(accountFrom, sum);
                addMoney(accountTo, sum);

                /*save*/
                transferRepository.saveAll(List.of(accountFrom, accountTo));

                log.info(
                        "Money transferred successfully! \n" +
                                "From account id=" + accountFrom.getId()
                                + " to account id=" + accountTo.getId()
                                + " sum=" + sum);

            }
        } catch (EntityNotFoundException e) {
            throw new AccountValidationException("Unable to find account with "
                    + e.getMessage().substring(e.getMessage().lastIndexOf("id")), e);
        }
    }

    /**
     * Add money to account operation
     */
    private void addMoney(Account account, Double sum) {
        account.setBalance(account.getBalance().add(BigDecimal.valueOf(sum)));
    }

    /**
     * Subtract money from account operation
     */
    private void subtractMoney(Account account, Double sum) {
        account.setBalance(account.getBalance().subtract(BigDecimal.valueOf(sum)));
    }
}
