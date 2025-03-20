package vn.com.msb.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    public void testHandleCustomerNotFoundException() {
        CustomerNotFoundException ex = new CustomerNotFoundException("Customer not found with id: 1");
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleCustomerNotFoundException(ex);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Customer not found with id: 1", response.getBody().getMessage());
    }

    @Test
    public void testHandleDuplicateCustomerCodeException() {
        DuplicateCustomerCodeException ex = new DuplicateCustomerCodeException("Customer code already exists: CUST001");
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDuplicateCustomerCodeException(ex);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatus());
        assertEquals("Customer code already exists: CUST001", response.getBody().getMessage());
    }

    @Test
    public void testHandleGenericException() {
        Exception ex = new RuntimeException("Unexpected error");
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(ex);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("An unexpected error occurred: Unexpected error", response.getBody().getMessage());
    }
}
