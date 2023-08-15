package com.fomichev.moneytransfer.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serializable;

@Data
public class RequestTransfer implements Serializable {

    @JsonProperty("fromAccountId")
    @NotNull
    @Min(value = 1000000000, message = "Account number should contains at least 10 digits")
    Long fromAccountId;

    @JsonProperty("toAccountId")
    @NotNull
    @Min(value = 1000000000, message = "Account number should contains at least 10 digits")
    Long toAccountId;

    @JsonProperty("sum")
    @NotNull
    @Positive
    Double sum;

}
