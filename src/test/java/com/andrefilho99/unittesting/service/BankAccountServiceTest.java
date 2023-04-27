package com.andrefilho99.unittesting.service;

import com.andrefilho99.unittesting.domain.BankAccount;
import com.andrefilho99.unittesting.exceptions.BankAccountNotFoundException;
import com.andrefilho99.unittesting.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @InjectMocks
    private BankAccountService bankAccountService;

    @Mock
    private BankAccountRepository bankAccountRepository;

    private BankAccount existingBankAccount;
    private BankAccount updatedBankAccount;
    private BankAccount newBankAccount;

    @BeforeEach
    public void before() {
        existingBankAccount = new BankAccount(1L, "1-1234", 1000.00);
        updatedBankAccount = new BankAccount(1L, "2-1234", 1250.00);
        newBankAccount = new BankAccount(null, "1-1234", 1000.00);
    }

    @Test
    public void getAll_NoBankAccountsCreated_ReturnEmptyList() {
        when(bankAccountRepository.findAll()).thenReturn(Arrays.asList());

        List<BankAccount> bankAccounts = bankAccountService.getAll();

        assertEquals(true, bankAccounts.isEmpty());
    }

    @Test
    public void getAll_OneBankAccountCreated_ReturnAccountInAList() {
        when(bankAccountRepository.findAll())
                .thenReturn(Arrays.asList(new BankAccount(1L, "1-1234", 1000.00)));

        List<BankAccount> bankAccounts = bankAccountService.getAll();

        assertEquals(false, bankAccounts.isEmpty());
        assertEquals(true, (bankAccounts.size() == 1));
        assertEquals(1L, bankAccounts.get(0).getId());
        assertEquals("1-1234", bankAccounts.get(0).getNumber());
        assertEquals(1000.00, bankAccounts.get(0).getBalance());
    }

    @Test
    public void getById_BankAccountWithId1Created_ReturnAccountWithId1() {
        when(bankAccountRepository.findById(1L))
                .thenReturn(Optional.of(existingBankAccount));

        BankAccount bankAccount = bankAccountService.getById(1L);

        assertEquals(1L, bankAccount.getId());
        assertEquals("1-1234", bankAccount.getNumber());
        assertEquals(1000.00, bankAccount.getBalance());
    }

    @Test
    public void getById_BankAccountWithId1NotPresent_ThrowBankAccountNotFoundException() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(BankAccountNotFoundException.class, () -> bankAccountService.getById(1L));

        assertEquals("Bank account with id 1 not found.", exception.getMessage());
    }

    @Test
    public void create_CreateBankAccount_ReturnAccountWithId1() {
        when(bankAccountRepository.save(newBankAccount))
                .thenReturn(existingBankAccount);

        BankAccount savedBankAccount = bankAccountService.create(newBankAccount);

        assertEquals(1L, savedBankAccount.getId());
        assertEquals("1-1234", savedBankAccount.getNumber());
        assertEquals(1000.00, savedBankAccount.getBalance());
    }

    @Test
    public void update_BankAccountWithId1UpdateNumberAndBalance_ReturnAccountWithId1AndUpdatedNumberAndBalance() {
        newBankAccount = new BankAccount(null, "2-1234", 1250.00);

        when(bankAccountRepository.findById(1L))
                .thenReturn(Optional.of(existingBankAccount));

        when(bankAccountRepository.save(updatedBankAccount))
                .thenReturn(updatedBankAccount);

        BankAccount savedBankAccount = bankAccountService.update(1L, newBankAccount);

        assertEquals(1L, savedBankAccount.getId());
        assertEquals("2-1234", savedBankAccount.getNumber());
        assertEquals(1250.00, savedBankAccount.getBalance());
    }

    @Test
    public void update_AccountWithId1NotPresent_ThrowBankAccountNotFoundException() {
        newBankAccount = new BankAccount(null, "2-1234", 1250.00);

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(BankAccountNotFoundException.class, () -> bankAccountService.update(1L, newBankAccount));

        assertEquals("Bank account with id 1 not found.", exception.getMessage());
    }

    @Test
    public void delete_DeleteAccountWithId1_Success() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(existingBankAccount));

        bankAccountService.delete(1L);
    }

    @Test
    public void delete_AccountWithId1NotPresent_ThrowBankAccountNotFoundException() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(BankAccountNotFoundException.class, () -> bankAccountService.delete(1L));
    }
}
