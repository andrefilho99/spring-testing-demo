package com.andrefilho99.unittesting.repository;

import com.andrefilho99.unittesting.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    public BankAccount findByNumber(String number);
}
