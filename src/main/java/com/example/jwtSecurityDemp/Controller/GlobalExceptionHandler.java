package com.example.jwtSecurityDemp.Controller;

import com.example.jwtSecurityDemp.expection.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = JwtAuthenticationException.class)
    @ResponseBody
    public ResponseEntity<Object> handleJwtAuthenticationException(JwtAuthenticationException ex) {
        System.out.println("inside handleJwtAuthenticationException");
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
