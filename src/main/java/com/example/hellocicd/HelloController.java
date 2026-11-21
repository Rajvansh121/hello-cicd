package com.example.hellocicd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Hello from CI/CD!";
    }

    @GetMapping("/health")
    public String health() {
        return "Application is UP!";
    }
}
