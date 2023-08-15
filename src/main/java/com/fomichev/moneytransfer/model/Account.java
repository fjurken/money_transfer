package com.fomichev.moneytransfer.model;

import com.fomichev.moneytransfer.EntityStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "accounts", initialValue = 1000000000)
    Long id;

    @CreatedDate
    Timestamp created;

    @LastModifiedDate
    Timestamp updated;

    BigDecimal balance;

    @Enumerated(EnumType.STRING)
    EntityStatus status;

}
