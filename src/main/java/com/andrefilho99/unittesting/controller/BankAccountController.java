package com.andrefilho99.unittesting.controller;

import com.andrefilho99.unittesting.domain.BankAccount;
import com.andrefilho99.unittesting.dto.BankAccountRequest;
import com.andrefilho99.unittesting.dto.BankAccountResponse;
import com.andrefilho99.unittesting.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bankAccounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<BankAccountResponse>> getAll() {
        List<BankAccount> bankAccountList = bankAccountService.getAll();
        List<BankAccountResponse> bankAccountResponseList = bankAccountList
                .stream()
                .map(bankAccount -> modelMapper.map(bankAccount, BankAccountResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(bankAccountResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountResponse> getById(@PathVariable Long id) {
        BankAccount bankAccount = bankAccountService.getById(id);
        BankAccountResponse bankAccountResponse = modelMapper.map(bankAccount, BankAccountResponse.class);

        return ResponseEntity.status(HttpStatus.OK).body(bankAccountResponse);
    }

    @PostMapping
    public ResponseEntity<BankAccountResponse> create(@RequestBody BankAccountRequest bankAccountRequest) {
        BankAccount bankAccount = bankAccountService.create(modelMapper.map(bankAccountRequest, BankAccount.class));
        BankAccountResponse bankAccountResponse = modelMapper.map(bankAccount, BankAccountResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(bankAccountResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankAccountResponse> update(@PathVariable Long id, @RequestBody BankAccountRequest bankAccountRequest) {
        BankAccount bankAccount = bankAccountService.update(id, modelMapper.map(bankAccountRequest, BankAccount.class));
        BankAccountResponse bankAccountResponse = modelMapper.map(bankAccount, BankAccountResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(bankAccountResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BankAccount> delete(@PathVariable Long id) {
        bankAccountService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
