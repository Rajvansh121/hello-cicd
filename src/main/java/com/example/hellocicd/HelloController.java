package com.example.hellocicd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Say yeahh there from my application CI/CD!";
    }

    @GetMapping("/health")
    public String health() {
        return "Application is UP and not Down !";
    }
}
