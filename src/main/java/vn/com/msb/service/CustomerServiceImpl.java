package vn.com.msb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.com.msb.exception.CustomerNotFoundException;
import vn.com.msb.exception.DuplicateCustomerCodeException;
import vn.com.msb.model.Customer;
import vn.com.msb.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }
    
    @Override
    public Customer getCustomerByCode(String customerCode) {
        return customerRepository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with code: " + customerCode));
    }
    
    @Override
    public List<Customer> searchCustomers(String keyword) {
        return customerRepository.search(keyword);
    }
    
    @Override
    public Customer createCustomer(Customer customer) {
        // Check if customer code already exists
        if (customerRepository.existsByCustomerCode(customer.getCustomerCode())) {
            throw new DuplicateCustomerCodeException("Customer code already exists: " + customer.getCustomerCode());
        }
        
        // Generate full name if not provided
        if (customer.getFullName() == null || customer.getFullName().isEmpty()) {
            customer.setFullName(customer.getFirstName() + " " + customer.getLastName());
        }
        
        return customerRepository.save(customer);
    }
    
    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        // Check if customer exists
        Customer existingCustomer = getCustomerById(id);
        
        // Check if customer code is being changed and already exists for another customer
        if (!existingCustomer.getCustomerCode().equals(customer.getCustomerCode()) &&
                customerRepository.existsByCustomerCodeAndIdNot(customer.getCustomerCode(), id)) {
            throw new DuplicateCustomerCodeException("Customer code already exists: " + customer.getCustomerCode());
        }
        
        // Update customer data
        customer.setId(id);
        
        // Generate full name if not provided
        if (customer.getFullName() == null || customer.getFullName().isEmpty()) {
            customer.setFullName(customer.getFirstName() + " " + customer.getLastName());
        }
        
        return customerRepository.save(customer);
    }
    
    @Override
    public void deleteCustomer(Long id) {
        // Check if customer exists
        getCustomerById(id);
        customerRepository.deleteById(id);
    }
}
