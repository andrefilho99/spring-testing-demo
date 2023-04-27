package com.andrefilho99.unittesting.service;

import com.andrefilho99.unittesting.domain.BankAccount;
import com.andrefilho99.unittesting.exceptions.BankAccountNotFoundException;
import com.andrefilho99.unittesting.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public List<BankAccount> getAll() {
        return bankAccountRepository.findAll();
    }

    public BankAccount getById(Long id) {
        return bankAccountRepository.findById(id).orElseThrow(
                () -> new BankAccountNotFoundException(String.format("Bank account with id %d not found.", id))
        );
    }

    public BankAccount create(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    public BankAccount update(Long id, BankAccount bankAccount) {
        BankAccount updatedBankAccount = getById(id);

        updatedBankAccount.setNumber(bankAccount.getNumber());
        updatedBankAccount.setBalance(bankAccount.getBalance());

        return bankAccountRepository.save(updatedBankAccount);
    }

    public void delete(Long id) {
        BankAccount bankAccount = getById(id);

        bankAccountRepository.delete(bankAccount);
    }
}
