package vn.com.msb.service;

import org.springframework.stereotype.Service;

import vn.com.msb.model.Greeting;

@Service
public class GreetingService {

    public Greeting getGreeting() {
        return new Greeting(1L, "Hello, World!");
    }
}