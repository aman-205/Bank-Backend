package com.aman.BankBackend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class Hell {

    @GetMapping("/ok")
    public String show(){
        return "hello";
    }
}
