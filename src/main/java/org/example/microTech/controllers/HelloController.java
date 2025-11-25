package org.example.microTech.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Hello Micro Tech!";
    }

    @GetMapping("/test")
    public String test() {
        return "ClientController working";
    }
}
