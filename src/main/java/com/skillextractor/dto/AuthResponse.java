// AuthResponse.java
package com.skillextractor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private Long userId;
    private String username;
    private String email;
    private String message;
}