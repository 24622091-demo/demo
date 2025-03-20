package vn.com.msb.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import vn.com.msb.exception.CustomerNotFoundException;
import vn.com.msb.exception.DuplicateCustomerCodeException;
import vn.com.msb.model.Customer;
import vn.com.msb.service.CustomerService;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private Customer customer;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // For LocalDate serialization
        
        customer = new Customer();
        customer.setId(1L);
        customer.setCustomerCode("CUST001");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setFullName("John Doe");
        customer.setDateOfBirth(LocalDate.of(1990, 1, 15));
        customer.setGender("Male");
        customer.setAddress("123 Main St");
        customer.setCity("New York");
        customer.setEmail("john.doe@example.com");
        customer.setPhoneNumber("555-123-4567");
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerCode", is("CUST001")));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        when(customerService.getCustomerById(anyLong())).thenReturn(customer);

        mockMvc.perform(get("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerCode", is("CUST001")));
    }

    @Test
    public void testGetCustomerByIdNotFound() throws Exception {
        when(customerService.getCustomerById(anyLong()))
                .thenThrow(new CustomerNotFoundException("Customer not found with id: 1"));

        mockMvc.perform(get("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetCustomerByCode() throws Exception {
        when(customerService.getCustomerByCode(anyString())).thenReturn(customer);

        mockMvc.perform(get("/api/customers/code/CUST001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerCode", is("CUST001")));
    }

    @Test
    public void testSearchCustomers() throws Exception {
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.searchCustomers(anyString())).thenReturn(customers);

        mockMvc.perform(get("/api/customers/search?keyword=John")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].fullName", is("John Doe")));
    }

    @Test
    public void testCreateCustomer() throws Exception {
        when(customerService.createCustomer(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerCode", is("CUST001")));
    }

    @Test
    public void testCreateCustomerDuplicateCode() throws Exception {
        when(customerService.createCustomer(any(Customer.class)))
                .thenThrow(new DuplicateCustomerCodeException("Customer code already exists: CUST001"));

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        when(customerService.updateCustomer(anyLong(), any(Customer.class))).thenReturn(customer);

        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerCode", is("CUST001")));
    }

    @Test
    public void testUpdateCustomerNotFound() throws Exception {
        when(customerService.updateCustomer(anyLong(), any(Customer.class)))
                .thenThrow(new CustomerNotFoundException("Customer not found with id: 1"));

        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(anyLong());

        mockMvc.perform(delete("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteCustomerNotFound() throws Exception {
        doThrow(new CustomerNotFoundException("Customer not found with id: 1"))
                .when(customerService).deleteCustomer(anyLong());

        mockMvc.perform(delete("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
