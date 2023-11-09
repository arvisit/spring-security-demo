package com.learning.securitydemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloRestController {

    @GetMapping("/journalist")
    public String helloJournalist() {
        return "Hello Journalist";
    }

    @GetMapping("/user")
    public String helloUser() {
        return "Hello User";
    }

    @GetMapping("/administrator")
    public String helloAdministrator() {
        return "Hello Administrator";
    }
}
