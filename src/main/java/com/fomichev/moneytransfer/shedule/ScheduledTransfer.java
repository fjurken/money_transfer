package com.fomichev.moneytransfer.shedule;

import com.fomichev.moneytransfer.controller.dto.AccountTo;
import com.fomichev.moneytransfer.exception.AccountListsNotMatches;
import com.fomichev.moneytransfer.exception.AccountValidationException;
import com.fomichev.moneytransfer.service.transfer.MoneyTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@RefreshScope
@Component
@EnableScheduling
public class ScheduledTransfer {

    private MoneyTransferService moneyTransferService;

    @Value("#{${accountsFrom}}")
    private Map<Long, Double> accountsFrom;

    @Value("${accountsTo}")
    private List<Long> accountsTo;

    public ScheduledTransfer(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }

    @Scheduled(cron = "@midnight")
    @Transactional
    public void transfer() throws AccountListsNotMatches, AccountValidationException {

        log.info("Scheduled task initiated");

        if (accountsFrom.size() != accountsTo.size() || (accountsFrom.containsKey(null) || accountsTo.contains(null))) {
            throw new AccountListsNotMatches(
                    "Account lists FROM and TO have different sizes! \n" +
                            "Operation can't be proceed!", null);
        }

        /*associate accounts FROM and TO in one map*/
        Map<Long, Long> accounts = new HashMap<>();

        Iterator<Long> accFrom = accountsFrom.keySet().iterator();
        Iterator<Long> accTo = accountsTo.iterator();
        while (accFrom.hasNext() && accTo.hasNext()) {
            accounts.put(accFrom.next(), accTo.next());
        }

        /*add transfer sum*/
        Map<Long, AccountTo<Long, Double>> transferList = new HashMap<>();

        accounts.entrySet().forEach(it ->
                transferList.put(
                        it.getKey(),
                        new AccountTo<>(it.getValue(), accountsFrom.get(it.getKey())))
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

