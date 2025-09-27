package com.software.spring.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class blogController {
    @GetMapping(value = "/")
    public String index (){
        return "index";
    }
}
