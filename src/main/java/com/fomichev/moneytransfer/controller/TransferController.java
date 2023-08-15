package com.fomichev.moneytransfer.controller;

import com.fomichev.moneytransfer.controller.dto.request.RequestTransfer;
import com.fomichev.moneytransfer.exception.AccountValidationException;
import com.fomichev.moneytransfer.service.transfer.MoneyTransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

/**
 * Money transfer controller
 */

@RestController
@RequestMapping("/")
public class TransferController {

    private final MoneyTransferService moneyTransferService;

    @Autowired
    public TransferController(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }


    @PostMapping("transfer")
    public ResponseEntity transfer(
            @Valid
            @RequestBody
            RequestTransfer request,
            Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.ok().body(
                    errors.getFieldError().getField() + " " + errors.getFieldError().getDefaultMessage());
        }
        try {
            moneyTransferService.transfer(request);
        } catch (AccountValidationException e) {
            return ResponseEntity.ok().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Money transferred successfully");
    }

}
