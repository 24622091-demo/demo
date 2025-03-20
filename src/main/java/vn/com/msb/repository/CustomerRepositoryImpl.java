package vn.com.msb.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import vn.com.msb.model.Customer;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {
    
    private final Map<Long, Customer> customerStore = new HashMap<>();
    private Long nextId = 1L;
    
    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customerStore.values());
    }
    
    @Override
    public Optional<Customer> findById(Long id) {
        return Optional.ofNullable(customerStore.get(id));
    }
    
    @Override
    public Optional<Customer> findByCustomerCode(String customerCode) {
        return customerStore.values().stream()
                .filter(customer -> customer.getCustomerCode().equals(customerCode))
                .findFirst();
    }
    
    @Override
    public List<Customer> findByFullNameContaining(String fullName) {
        return customerStore.values().stream()
                .filter(customer -> customer.getFullName().toLowerCase().contains(fullName.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Customer> findByEmailContaining(String email) {
        return customerStore.values().stream()
                .filter(customer -> customer.getEmail().toLowerCase().contains(email.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Customer> findByPhoneNumberContaining(String phoneNumber) {
        return customerStore.values().stream()
                .filter(customer -> customer.getPhoneNumber().contains(phoneNumber))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Customer> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        
        String searchTerm = keyword.toLowerCase().trim();
        
        return customerStore.values().stream()
                .filter(customer -> 
                    (customer.getFullName() != null && customer.getFullName().toLowerCase().contains(searchTerm)) ||
                    (customer.getEmail() != null && customer.getEmail().toLowerCase().contains(searchTerm)) ||
                    (customer.getPhoneNumber() != null && customer.getPhoneNumber().contains(searchTerm)) ||
                    (customer.getCustomerCode() != null && customer.getCustomerCode().toLowerCase().contains(searchTerm))
                )
                .collect(Collectors.toList());
    }
    
    @Override
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            // Creating a new customer
            customer.setId(nextId++);
        }
        customerStore.put(customer.getId(), customer);
        return customer;
    }
    
    @Override
    public void deleteById(Long id) {
        customerStore.remove(id);
    }
    
    @Override
    public boolean existsByCustomerCode(String customerCode) {
        return customerStore.values().stream()
                .anyMatch(customer -> customer.getCustomerCode().equals(customerCode));
    }
    
    @Override
    public boolean existsByCustomerCodeAndIdNot(String customerCode, Long id) {
        return customerStore.values().stream()
                .anyMatch(customer -> customer.getCustomerCode().equals(customerCode) && !customer.getId().equals(id));
    }
}
