package com.example.Backend.Response.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean status;
    private String message;
    private T data;

        public ApiResponse(boolean status, String message, T data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }
}
