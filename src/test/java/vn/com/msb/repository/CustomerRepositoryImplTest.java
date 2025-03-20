package vn.com.msb.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import vn.com.msb.model.Customer;

public class CustomerRepositoryImplTest {

    private CustomerRepositoryImpl customerRepository;
    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    public void setup() {
        customerRepository = new CustomerRepositoryImpl();
        
        // Create test customers
        customer1 = new Customer();
        customer1.setCustomerCode("CUST001");
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        customer1.setFullName("John Doe");
        customer1.setEmail("john.doe@example.com");
        customer1.setPhoneNumber("555-123-4567");
        customer1.setDateOfBirth(LocalDate.of(1990, 1, 15));
        customer1.setGender("Male");
        customer1.setAddress("123 Main St");
        customer1.setCity("New York");
        
        customer2 = new Customer();
        customer2.setCustomerCode("CUST002");
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");
        customer2.setFullName("Jane Smith");
        customer2.setEmail("jane.smith@example.com");
        customer2.setPhoneNumber("555-987-6543");
        customer2.setDateOfBirth(LocalDate.of(1992, 5, 20));
        customer2.setGender("Female");
        customer2.setAddress("456 Oak Ave");
        customer2.setCity("Boston");
        
        // Save customers to repository
        customerRepository.save(customer1);
        customerRepository.save(customer2);
    }

    @Test
    public void testFindAll() {
        List<Customer> customers = customerRepository.findAll();
        
        assertEquals(2, customers.size());
        assertTrue(customers.stream().anyMatch(c -> c.getCustomerCode().equals("CUST001")));
        assertTrue(customers.stream().anyMatch(c -> c.getCustomerCode().equals("CUST002")));
    }

    @Test
    public void testFindById() {
        Optional<Customer> found = customerRepository.findById(1L);
        
        assertTrue(found.isPresent());
        assertEquals("CUST001", found.get().getCustomerCode());
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<Customer> found = customerRepository.findById(99L);
        
        assertFalse(found.isPresent());
    }

    @Test
    public void testFindByCustomerCode() {
        Optional<Customer> found = customerRepository.findByCustomerCode("CUST001");
        
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
    }

    @Test
    public void testFindByFullNameContaining() {
        List<Customer> customers = customerRepository.findByFullNameContaining("John");
        
        assertEquals(1, customers.size());
        assertEquals("CUST001", customers.get(0).getCustomerCode());
    }

    @Test
    public void testFindByEmailContaining() {
        List<Customer> customers = customerRepository.findByEmailContaining("jane");
        
        assertEquals(1, customers.size());
        assertEquals("CUST002", customers.get(0).getCustomerCode());
    }

    @Test
    public void testFindByPhoneNumberContaining() {
        List<Customer> customers = customerRepository.findByPhoneNumberContaining("987");
        
        assertEquals(1, customers.size());
        assertEquals("CUST002", customers.get(0).getCustomerCode());
    }

    @Test
    public void testSearch() {
        // Test search by full name
        List<Customer> customers1 = customerRepository.search("John");
        assertEquals(1, customers1.size());
        assertEquals("CUST001", customers1.get(0).getCustomerCode());
        
        // Test search by email
        List<Customer> customers2 = customerRepository.search("jane.smith");
        assertEquals(1, customers2.size());
        assertEquals("CUST002", customers2.get(0).getCustomerCode());
        
        // Test search by phone number
        List<Customer> customers3 = customerRepository.search("987");
        assertEquals(1, customers3.size());
        assertEquals("CUST002", customers3.get(0).getCustomerCode());
        
        // Test search by customer code
        List<Customer> customers4 = customerRepository.search("CUST001");
        assertEquals(1, customers4.size());
        assertEquals("John", customers4.get(0).getFirstName());
    }

    @Test
    public void testSaveCreate() {
        Customer customer3 = new Customer();
        customer3.setCustomerCode("CUST003");
        customer3.setFirstName("Bob");
        customer3.setLastName("Johnson");
        customer3.setFullName("Bob Johnson");
        
        Customer saved = customerRepository.save(customer3);
        
        // Check ID is assigned
        assertTrue(saved.getId() > 0);
        assertEquals("CUST003", saved.getCustomerCode());
        
        // Check it's in the repository
        Optional<Customer> found = customerRepository.findByCustomerCode("CUST003");
        assertTrue(found.isPresent());
    }

    @Test
    public void testSaveUpdate() {
        // Get customer1 ID
        Long id = customer1.getId();
        
        // Update customer
        customer1.setFirstName("Jonathan");
        customer1.setFullName("Jonathan Doe");
        
        Customer updated = customerRepository.save(customer1);
        
        // Verify update
        assertEquals(id, updated.getId()); // Same ID
        assertEquals("Jonathan", updated.getFirstName());
        assertEquals("Jonathan Doe", updated.getFullName());
        
        // Check in repository
        Optional<Customer> found = customerRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals("Jonathan", found.get().getFirstName());
    }

    @Test
    public void testDeleteById() {
        Long id = customer1.getId();
        
        customerRepository.deleteById(id);
        
        // Verify it's deleted
        Optional<Customer> found = customerRepository.findById(id);
        assertFalse(found.isPresent());
        
        // Verify count is now 1
        List<Customer> customers = customerRepository.findAll();
        assertEquals(1, customers.size());
    }

    @Test
    public void testExistsByCustomerCode() {
        assertTrue(customerRepository.existsByCustomerCode("CUST001"));
        assertFalse(customerRepository.existsByCustomerCode("NONEXISTENT"));
    }

    @Test
    public void testExistsByCustomerCodeAndIdNot() {
        Long id1 = customer1.getId();
        
        // Should return false for same customer
        assertFalse(customerRepository.existsByCustomerCodeAndIdNot("CUST001", id1));
        
        // Should return true for different customer
        assertTrue(customerRepository.existsByCustomerCodeAndIdNot("CUST002", id1));
        
        // Should return false for non-existent code
        assertFalse(customerRepository.existsByCustomerCodeAndIdNot("NONEXISTENT", id1));
    }
}
