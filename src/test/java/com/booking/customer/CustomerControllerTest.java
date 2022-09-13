package com.booking.customer;

import com.booking.App;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void before() {
        customerRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        customerRepository.deleteAll();
    }

    @Test
    public void shouldInvokeSignUpEndPoint() throws Exception {
        String uri = "/sign-up";
        Customer customer = new Customer();
        customer.setUsername("user@1");
        customer.setFullName("Test User");
        customer.setEmail("demo@gmail.com");
        customer.setPhoneNumber("9876543210");
        customer.setPassword("DemoUser@1");
        customer.setConfirmPassword("DemoUser@1");
        String requestJson = objectMapper.writeValueAsString(customer);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("Sign up successful", mvcResult.getResponse().getContentAsString());
    }
}
