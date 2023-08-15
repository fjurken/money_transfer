package com.fomichev.moneytransfer.shedule;

import com.fomichev.moneytransfer.config.AccountsConfiguration;
import com.fomichev.moneytransfer.controller.dto.AccountTo;
import com.fomichev.moneytransfer.exception.AccountListsNotMatches;
import com.fomichev.moneytransfer.exception.AccountValidationException;
import com.fomichev.moneytransfer.service.transfer.MoneyTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Scheduled class
 * Used to transfer money between accounts every midnight
 */
@Slf4j
@Component
@EnableScheduling
public class ScheduledTransfer {

    private final MoneyTransferService moneyTransferService;
    private final AccountsConfiguration accountsConfig;

    @Autowired
    public ScheduledTransfer(MoneyTransferService moneyTransferService, AccountsConfiguration accountsConfig) {
        this.moneyTransferService = moneyTransferService;
        this.accountsConfig = accountsConfig;
    }

    /**
     * Transfer money between accounts from the list
     */
    @Scheduled(cron = "@midnight")
    @Transactional
    public void transfer() throws AccountListsNotMatches, AccountValidationException {

        log.info("Scheduled task initiated");

        if (accountsConfig.accountsFrom.size() != accountsConfig.accountsTo.size()
                || (accountsConfig.accountsFrom.containsKey(null) || accountsConfig.accountsTo.contains(null))) {
            throw new AccountListsNotMatches(
                    "Account lists FROM and TO have different sizes! \n" +
                            "Operation can't be proceed!", null);
        }

        /*associate accounts FROM and TO in one map*/
        Map<Long, Long> accounts = new HashMap<>();

        Iterator<Long> accFrom = accountsConfig.accountsFrom.keySet().iterator();
        Iterator<Long> accTo = accountsConfig.accountsTo.iterator();
        while (accFrom.hasNext() && accTo.hasNext()) {
            accounts.put(accFrom.next(), accTo.next());
        }

        /*add transfer sum*/
        Map<Long, AccountTo<Long, Double>> transferList = new HashMap<>();

        accounts.entrySet().forEach(it ->
                transferList.put(
                        it.getKey(),
                        new AccountTo<>(it.getValue(), accountsConfig.accountsFrom.get(it.getKey())))
        );

        /*general information about operation*/
        log.info("Ready to transfer:");
        transferList
                .entrySet()
                .forEach(
                        transfer -> System.out.println(
                                "Account from id=" + transfer.getKey() +
                                        " account to id=" + transfer.getValue().accountId +
                                        " sum=" + transfer.getValue().sum));

        /*transfer*/
        moneyTransferService.transfer(transferList);

    }
}

