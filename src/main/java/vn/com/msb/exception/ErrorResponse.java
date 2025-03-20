package vn.com.msb.exception;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error response information")
public class ErrorResponse {
    
    @Schema(description = "HTTP status code", example = "404")
    private int status;
    
    @Schema(description = "Error message", example = "Customer not found with id: 1")
    private String message;
    
    @Schema(description = "Timestamp when the error occurred", example = "2023-10-25T15:30:45.123")
    private LocalDateTime timestamp;
    
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
