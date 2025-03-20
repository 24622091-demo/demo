package vn.com.msb.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import vn.com.msb.exception.CustomerNotFoundException;
import vn.com.msb.exception.DuplicateCustomerCodeException;
import vn.com.msb.model.Customer;
import vn.com.msb.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    public void setup() {
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
    public void testGetAllCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CUST001", result.get(0).getCustomerCode());
    }

    @Test
    public void testGetCustomerById() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("CUST001", result.getCustomerCode());
    }

    @Test
    public void testGetCustomerByIdNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomerById(1L);
        });
    }

    @Test
    public void testGetCustomerByCode() {
        when(customerRepository.findByCustomerCode(anyString())).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerByCode("CUST001");

        assertNotNull(result);
        assertEquals("CUST001", result.getCustomerCode());
    }

    @Test
    public void testGetCustomerByCodeNotFound() {
        when(customerRepository.findByCustomerCode(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomerByCode("CUST001");
        });
    }

    @Test
    public void testSearchCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.search(anyString())).thenReturn(customers);

        List<Customer> result = customerService.searchCustomers("John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
    }

    @Test
    public void testCreateCustomer() {
        when(customerRepository.existsByCustomerCode(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer newCustomer = new Customer();
        newCustomer.setCustomerCode("CUST001");
        newCustomer.setFirstName("John");
        newCustomer.setLastName("Doe");
        
        Customer result = customerService.createCustomer(newCustomer);

        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testCreateCustomerWithDuplicateCode() {
        when(customerRepository.existsByCustomerCode(anyString())).thenReturn(true);

        Customer newCustomer = new Customer();
        newCustomer.setCustomerCode("CUST001");
        newCustomer.setFirstName("John");
        newCustomer.setLastName("Doe");

        assertThrows(DuplicateCustomerCodeException.class, () -> {
            customerService.createCustomer(newCustomer);
        });
    }

    @Test
    public void testUpdateCustomer() {
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setCustomerCode("CUST0010");
        
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.existsByCustomerCodeAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerCode("CUST0011");
        updatedCustomer.setFirstName("John");
        updatedCustomer.setLastName("Doe");
        
        Customer result = customerService.updateCustomer(1L, updatedCustomer);

        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testUpdateCustomerWithDuplicateCode() {
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setCustomerCode("CUST001");
        
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.existsByCustomerCodeAndIdNot(anyString(), anyLong())).thenReturn(true);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerCode("CUST002"); // Different code
        
        assertThrows(DuplicateCustomerCodeException.class, () -> {
            customerService.updateCustomer(1L, updatedCustomer);
        });
    }

    @Test
    public void testDeleteCustomer() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).deleteById(anyLong());

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteCustomerNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.deleteCustomer(1L);
        });
    }
}
