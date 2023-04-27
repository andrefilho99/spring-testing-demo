package com.andrefilho99.unittesting.repository;

import com.andrefilho99.unittesting.domain.BankAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BankAccountRepositoryTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Test
    public void findByNumber_BankAccountCreated_ReturnBankAccountWithSpecifiedNumber() {
        BankAccount savedBankAccount =  bankAccountRepository.save(new BankAccount(null, "1-2345", 1000.00));
        BankAccount bankAccount = bankAccountRepository.findByNumber("1-2345");

        assertEquals(savedBankAccount.getId(), bankAccount.getId());
        assertEquals(savedBankAccount.getNumber(), bankAccount.getNumber());
        assertEquals(savedBankAccount.getBalance(), bankAccount.getBalance());
    }
}
