package com.example.palindromeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PalindromeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PalindromeApiApplication.class, args);
    }

}
