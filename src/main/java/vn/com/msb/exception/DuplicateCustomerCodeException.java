package vn.com.msb.exception;

public class DuplicateCustomerCodeException extends RuntimeException {
    public DuplicateCustomerCodeException(String message) {
        super(message);
    }
}
