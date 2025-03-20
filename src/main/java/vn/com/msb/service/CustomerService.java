package vn.com.msb.service;

import java.util.List;

import vn.com.msb.model.Customer;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long id);
    Customer getCustomerByCode(String customerCode);
    List<Customer> searchCustomers(String keyword);
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Long id, Customer customer);
    void deleteCustomer(Long id);
}
