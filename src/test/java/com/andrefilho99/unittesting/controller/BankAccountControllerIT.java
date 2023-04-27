package com.andrefilho99.unittesting.controller;

import com.andrefilho99.unittesting.dto.BankAccountResponse;
import com.andrefilho99.unittesting.exceptions.ErrorResponse;
import com.andrefilho99.unittesting.repository.BankAccountRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankAccountControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Test
    @Order(1)
    public void getAll_NoBankAccountsCreated_ReturnNOAccountsAndStatus200() {
        ResponseEntity<List<BankAccountResponse>> response = restTemplate.exchange(
                "/bankAccounts",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BankAccountResponse>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("[]", response.getBody().toString());
    }

    @Test
    @Order(2)
    public void create_CreateBankAccount_ReturnAccountAndStatus201() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("number", "1-2345");
        requestBody.put("balance", 1000.00);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<BankAccountResponse> response = restTemplate.exchange(
                "/bankAccounts",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<BankAccountResponse>() {}
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("1-2345", response.getBody().getNumber());
        assertEquals(1000.00, response.getBody().getBalance());
    }

    @Test
    @Order(3)
    public void getAll_OneBankAccountCreated_ReturnAccountInArrayAndStatus200() {
        ResponseEntity<List<BankAccountResponse>> response = restTemplate.exchange(
                "/bankAccounts",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BankAccountResponse>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
        assertEquals("1-2345", response.getBody().get(0).getNumber());
        assertEquals(1000.00, response.getBody().get(0).getBalance());
    }

    @Test
    @Order(4)
    public void getById_BankAccountWithId1Created_ReturnAccountWithId1AndStatus200() {
        ResponseEntity<BankAccountResponse> response = restTemplate.exchange(
                "/bankAccounts/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BankAccountResponse>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        assertEquals("1-2345", response.getBody().getNumber());
        assertEquals(1000.00, response.getBody().getBalance());
    }

    @Test
    @Order(5)
    public void update_BankAccountWithId1UpdateNumberAndBalance_ReturnAccountWithId1AndUpdatedNumberAndBalanceAndStatus200() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("number", "2-2345");
        requestBody.put("balance", 2000.00);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<BankAccountResponse> response = restTemplate.exchange(
                "/bankAccounts/1",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<BankAccountResponse>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        assertEquals("2-2345", response.getBody().getNumber());
        assertEquals(2000.00, response.getBody().getBalance());
    }

    @Test
    @Order(6)
    public void delete_DeleteAccountWithId1_ReturnStatus204() {
        ResponseEntity response = restTemplate.exchange(
                "/bankAccounts/1",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(7)
    public void getById_BankAccountWithId1NotPresent_ReturnErrorMessageAndStatus404() {
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/bankAccounts/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ErrorResponse>() {}
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Bank account with id 1 not found.", response.getBody().getError());
    }

    @Test
    @Order(8)
    public void update_AccountWithId1NotPresent_ReturnStatus404() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("number", "2-2345");
        requestBody.put("balance", 2000.00);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/bankAccounts/1",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<ErrorResponse>() {}
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Bank account with id 1 not found.", response.getBody().getError());
    }

    @Test
    @Order(9)
    public void delete_AccountWithId1NotPresent_ReturnStatus404() {
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/bankAccounts/1",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Bank account with id 1 not found.", response.getBody().getError());
    }
}
