package com.fomichev.moneytransfer.service.transfer;

import com.fomichev.moneytransfer.controller.dto.AccountTo;
import com.fomichev.moneytransfer.controller.dto.request.RequestTransfer;
import com.fomichev.moneytransfer.exception.AccountValidationException;

import java.util.Map;

public interface MoneyTransferService {

    void transfer(RequestTransfer requestTransfer) throws AccountValidationException;

    void transfer(Map<Long, AccountTo<Long, Double>> transferList) throws AccountValidationException;

}
