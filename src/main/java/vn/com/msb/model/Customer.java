package vn.com.msb.model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Customer information")
public class Customer {
    
    @Schema(description = "Unique identifier of the customer", example = "1")
    private Long id;
    
    @Schema(description = "Unique customer code", example = "CUST001", required = true)
    private String customerCode;
    
    @Schema(description = "Customer's first name", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "Customer's last name", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "Customer's full name", example = "John Doe")
    private String fullName;
    
    @Schema(description = "Customer's date of birth", example = "1990-01-15")
    private LocalDate dateOfBirth;
    
    @Schema(description = "Customer's gender", example = "Male", allowableValues = {"Male", "Female", "Other"})
    private String gender;
    
    @Schema(description = "Customer's address", example = "123 Main St")
    private String address;
    
    @Schema(description = "Customer's city", example = "New York")
    private String city;
    
    @Schema(description = "Customer's email address", example = "john.doe@example.com")
    private String email;
    
    @Schema(description = "Customer's phone number", example = "555-123-4567")
    private String phoneNumber;

    public Customer() {
    }

    public Customer(Long id, String customerCode, String firstName, String lastName, 
                   String fullName, LocalDate dateOfBirth, String gender, 
                   String address, String city, String email, String phoneNumber) {
        this.id = id;
        this.customerCode = customerCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
