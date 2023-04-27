package com.andrefilho99.unittesting.controller;

import com.andrefilho99.unittesting.domain.BankAccount;
import com.andrefilho99.unittesting.dto.BankAccountRequest;
import com.andrefilho99.unittesting.dto.BankAccountResponse;
import com.andrefilho99.unittesting.exceptions.BankAccountNotFoundException;
import com.andrefilho99.unittesting.service.BankAccountService;
import com.google.gson.Gson;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankAccountController.class)
public class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private ModelMapper modelMapper;

    private BankAccountRequest bankAccountRequest;
    private BankAccount bankAccount;
    private BankAccountResponse bankAccountResponse;

    @BeforeEach
    public void before() {
        bankAccountRequest = new BankAccountRequest("1-1234", 1000.00);
        bankAccount = new BankAccount(null, "1-1234", 1000.00);
        bankAccountResponse = new BankAccountResponse(1L, "1-1234", 1000.00);
    }

    @Test
    public void getAll_NoBankAccountsCreated_ReturnNOAccountsAndStatus200() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/bankAccounts")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().is(200))
                .andExpect(content().string("[]"));
    }

    @Test
    public void getAll_OneBankAccountCreated_ReturnAccountInArrayAndStatus200() throws Exception {
        when(bankAccountService.getAll())
                .thenReturn(Arrays.asList(bankAccount));

        when(modelMapper.map(bankAccount, BankAccountResponse.class))
                .thenReturn(bankAccountResponse);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/bankAccounts")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].number").value("1-1234"))
                .andExpect(jsonPath("$[0].balance").value(BigDecimal.valueOf(1000.00)));
    }

    @Test
    public void getById_BankAccountWithId1Created_ReturnAccountWithId1AndStatus200() throws Exception {
        when(bankAccountService.getById(1L)).thenReturn(bankAccount);

        when(modelMapper.map(bankAccount, BankAccountResponse.class))
                .thenReturn(bankAccountResponse);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/bankAccounts/1")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("1-1234"))
                .andExpect(jsonPath("$.balance").value(BigDecimal.valueOf(1000.00)));
    }

    @Test
    public void getById_BankAccountWithId1NotPresent_ReturnErrorMessageAndStatus404() throws Exception {
        when(bankAccountService.getById(1L)).thenThrow(
                new BankAccountNotFoundException(String.format("Bank account with id %d not found.", 1))
        );

        RequestBuilder request = MockMvcRequestBuilders
                .get("/bankAccounts/1")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().is(404))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BankAccountNotFoundException))
                .andExpect(result -> assertEquals("Bank account with id 1 not found.", result.getResolvedException().getMessage()));
    }

    @Test
    public void create_CreateBankAccount_ReturnAccountWithId1AndStatus201() throws Exception {
        when(bankAccountService.create(bankAccount)).thenReturn(
                new BankAccount(1L, "1-1234", 1000.00)
        );

        when(modelMapper.map(any(), any()))
                .thenReturn(bankAccount)
                .thenReturn(bankAccountResponse);


        RequestBuilder request = MockMvcRequestBuilders
                .post("/bankAccounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(bankAccountRequest))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("1-1234"))
                .andExpect(jsonPath("$.balance").value(BigDecimal.valueOf(1000.00)));
    }

    @Test
    public void update_BankAccountWithId1UpdateNumberAndBalance_ReturnAccountWithId1AndUpdatedNumberAndBalanceAndStatus200() throws Exception {
        bankAccountRequest = new BankAccountRequest("2-1234", 1250.00);
        bankAccount = new BankAccount(null, "2-1234", 1000.00);
        bankAccountResponse = new BankAccountResponse(1L, "2-1234", 1250.00);

        when(bankAccountService.update(1L, bankAccount)).thenReturn(
                new BankAccount(1L, "2-1234", 1250.00)
        );

        when(modelMapper.map(any(), any()))
                .thenReturn(bankAccount)
                .thenReturn(bankAccountResponse);

        RequestBuilder request = MockMvcRequestBuilders
                .put("/bankAccounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(bankAccountRequest))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("2-1234"))
                .andExpect(jsonPath("$.balance").value(BigDecimal.valueOf(1250.00)));
    }

    @Test
    public void update_AccountWithId1NotPresent_ReturnStatus404() throws Exception {
        bankAccountRequest = new BankAccountRequest("2-1234", 1250.00);
        bankAccount = new BankAccount(null, "2-1234", 1000.00);

        when(bankAccountService.update(1L, bankAccount))
                .thenThrow(new BankAccountNotFoundException(String.format("Bank account with id %d not found.", 1)));

        when(modelMapper.map(bankAccountRequest, BankAccount.class))
                .thenReturn(bankAccount);

        RequestBuilder request = MockMvcRequestBuilders
                .put("/bankAccounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(bankAccountRequest));

        mockMvc.perform(request)
                .andExpect(status().is(404))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BankAccountNotFoundException))
                .andExpect(result -> assertEquals("Bank account with id 1 not found.", result.getResolvedException().getMessage()));
    }

    @Test
    public void delete_DeleteAccountWithId1_ReturnStatus204() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/bankAccounts/1");
        mockMvc.perform(request).andExpect(status().is(204));
    }

    @Test
    public void delete_AccountWithId1NotPresent_ReturnStatus404() throws Exception {
        doThrow(new BankAccountNotFoundException(String.format("Bank account with id %d not found.", 1)))
                .when(bankAccountService).delete(1L);

        RequestBuilder request = MockMvcRequestBuilders.delete("/bankAccounts/1");

        mockMvc.perform(request)
                .andExpect(status().is(404))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BankAccountNotFoundException))
                .andExpect(result -> assertEquals("Bank account with id 1 not found.", result.getResolvedException().getMessage()));
    }
}
