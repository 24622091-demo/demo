package vn.com.msb.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Test
    public void testFindAllWithEmptyRepository() {
        // Create a new repository instance to ensure it's empty
        CustomerRepositoryImpl emptyRepo = new CustomerRepositoryImpl();
        List<Customer> customers = emptyRepo.findAll();
        
        assertNotNull(customers);
        assertEquals(0, customers.size());
    }

    @Test
    public void testFindByFullNameCaseInsensitive() {
        // Test with different case
        List<Customer> customers = customerRepository.findByFullNameContaining("john");
        
        assertEquals(1, customers.size());
        assertEquals("CUST001", customers.get(0).getCustomerCode());
        
        // Test with uppercase
        customers = customerRepository.findByFullNameContaining("JOHN");
        
        assertEquals(1, customers.size());
        assertEquals("CUST001", customers.get(0).getCustomerCode());
    }
    
    @Test
    public void testFindByFullNameWithPartialMatch() {
        // Test partial name search
        List<Customer> customers = customerRepository.findByFullNameContaining("Jo");
        
        assertEquals(1, customers.size());
        assertEquals("CUST001", customers.get(0).getCustomerCode());
    }
    
    @Test
    public void testFindByFullNameNoMatch() {
        // Test with non-existent name
        List<Customer> customers = customerRepository.findByFullNameContaining("Robert");
        
        assertEquals(0, customers.size());
    }
    
    @Test
    public void testFindByEmailWithEmptyString() {
        List<Customer> customers = customerRepository.findByEmailContaining("");
        
        assertEquals(2, customers.size()); // Should return all customers
    }
    
    @Test
    public void testFindByEmailDomain() {
        // Test finding by email domain
        List<Customer> customers = customerRepository.findByEmailContaining("example.com");
        
        assertEquals(2, customers.size());
    }
    
    @Test
    public void testSearchWithNullKeyword() {
        List<Customer> customers = customerRepository.search(null);
        
        assertEquals(2, customers.size()); // Should return all customers
    }
    
    @Test
    public void testSearchWithEmptyKeyword() {
        List<Customer> customers = customerRepository.search("");
        
        assertEquals(2, customers.size()); // Should return all customers
    }
    
    @Test
    public void testSearchWithWhitespaceKeyword() {
        List<Customer> customers = customerRepository.search("   ");
        
        assertEquals(2, customers.size()); // Should return all customers
    }
    
    @Test
    public void testSearchWithMultipleMatches() {
        // First, add a customer that will match multiple criteria
        Customer customer3 = new Customer();
        customer3.setCustomerCode("CUST003");
        customer3.setFirstName("John"); // Same first name as customer1
        customer3.setLastName("Johnson");
        customer3.setFullName("John Johnson");
        customer3.setEmail("john.johnson@example.com"); // Contains "john" like customer1's email
        customer3.setPhoneNumber("555-123-7890"); // Contains "123" like customer1's phone
        
        customerRepository.save(customer3);
        
        // Search for "john" - should match multiple fields in multiple customers
        List<Customer> customers = customerRepository.search("john");
        
        assertEquals(2, customers.size());
        assertTrue(customers.stream().anyMatch(c -> c.getCustomerCode().equals("CUST001")));
        assertTrue(customers.stream().anyMatch(c -> c.getCustomerCode().equals("CUST003")));
    }
    
    @Test
    public void testSaveWithNullId() {
        Customer customer3 = new Customer();
        customer3.setId(null); // Explicitly set null
        customer3.setCustomerCode("CUST003");
        customer3.setFirstName("Bob");
        customer3.setLastName("Johnson");
        
        Customer saved = customerRepository.save(customer3);
        
        assertNotNull(saved.getId());
        assertTrue(saved.getId() > 0);
    }
    
    @Test
    public void testSaveWithExistingId() {
        // Create a new customer
        Customer customer3 = new Customer();
        customer3.setCustomerCode("CUST003");
        customer3.setFirstName("Bob");
        customer3.setLastName("Johnson");
        
        // Save to get an ID
        Customer saved = customerRepository.save(customer3);
        Long id = saved.getId();
        
        // Update and save again
        saved.setFirstName("Robert");
        Customer updated = customerRepository.save(saved);
        
        // ID should remain the same
        assertEquals(id, updated.getId());
        assertEquals("Robert", updated.getFirstName());
    }
    
    @Test
    public void testDeleteNonExistentId() {
        // This shouldn't throw an exception
        customerRepository.deleteById(99L);
        
        // Verify the existing customers weren't affected
        assertEquals(2, customerRepository.findAll().size());
    }
    
    @Test
    public void testExistsByCustomerCodeCaseSensitivity() {
        // Test with original case
        assertTrue(customerRepository.existsByCustomerCode("CUST001"));
        
        // Test with different case - should be case sensitive
        assertFalse(customerRepository.existsByCustomerCode("cust001"));
    }
    
    @Test
    public void testFindByCustomerCodeCaseSensitivity() {
        // Test with original case
        Optional<Customer> found1 = customerRepository.findByCustomerCode("CUST001");
        assertTrue(found1.isPresent());
        
        // Test with different case - should be case sensitive
        Optional<Customer> found2 = customerRepository.findByCustomerCode("cust001");
        assertFalse(found2.isPresent());
    }
    
    @Test
    public void testFindByNonExistentCustomerCode() {
        Optional<Customer> found = customerRepository.findByCustomerCode("NONEXISTENT");
        
        assertFalse(found.isPresent());
    }
    
    @Test
    public void testSaveMultipleCustomers() {
        // Add more customers
        for (int i = 3; i <= 10; i++) {
            Customer customer = new Customer();
            customer.setCustomerCode("CUST00" + i);
            customer.setFirstName("Customer");
            customer.setLastName("" + i);
            customerRepository.save(customer);
        }
        
        // Verify all customers were saved
        List<Customer> allCustomers = customerRepository.findAll();
        assertEquals(10, allCustomers.size());
        
        // Verify IDs are sequential
        boolean hasCustomer10 = allCustomers.stream()
                .anyMatch(c -> c.getCustomerCode().equals("CUST0010"));
        assertTrue(hasCustomer10);
    }
    
    @Test
    public void testFindByPhoneNumberPartialMatch() {
        // First test - find by area code
        List<Customer> customers1 = customerRepository.findByPhoneNumberContaining("555");
        assertEquals(2, customers1.size());
        
        // Second test - find by just a few digits
        List<Customer> customers2 = customerRepository.findByPhoneNumberContaining("23");
        assertEquals(1, customers2.size());
        assertEquals("CUST001", customers2.get(0).getCustomerCode());
    }
}
