package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model.Account;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {AccountController.class, AccountControllerTest.TestConfig.class})
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    private Account testAccount;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        testAccount = Account.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("password")
                .name("John")
                .nik("1234567890")
                .address("Somewhere")
                .phoneNumber("08123456789")
                .build();

        Mockito.reset(accountService);
    }

    @Test
    void testGetAccountById() throws Exception {
        Mockito.when(accountService.getAccountById(testAccount.getId().toString()))
                .thenReturn(testAccount);

        mockMvc.perform(get("/api/accounts/" + testAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testUpdateAccount() throws Exception {
        Mockito.when(accountService.updateAccount(any(Account.class)))
                .thenReturn(testAccount);

        mockMvc.perform(put("/api/accounts/" + testAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void testDeleteAccount() throws Exception {
        Mockito.doNothing().when(accountService).deleteAccount(eq(testAccount.getId().toString()));

        mockMvc.perform(delete("/api/accounts/" + testAccount.getId()))
                .andExpect(status().isOk());
    }

    @Configuration
    static class TestConfig {
        @Bean
        public AccountService accountService() {
            return Mockito.mock(AccountService.class);
        }
    }
}
