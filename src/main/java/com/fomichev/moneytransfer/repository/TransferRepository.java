package com.fomichev.moneytransfer.repository;

import com.fomichev.moneytransfer.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Account, Long> {
}
