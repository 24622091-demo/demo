package vn.com.msb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import vn.com.msb.model.Customer;

@Repository
public interface CustomerRepository {
    List<Customer> findAll();
    Optional<Customer> findById(Long id);
    Optional<Customer> findByCustomerCode(String customerCode);
    List<Customer> findByFullNameContaining(String fullName);
    List<Customer> findByEmailContaining(String email);
    List<Customer> findByPhoneNumberContaining(String phoneNumber);
    List<Customer> search(String keyword);
    Customer save(Customer customer);
    void deleteById(Long id);
    boolean existsByCustomerCode(String customerCode);
    boolean existsByCustomerCodeAndIdNot(String customerCode, Long id);
}
