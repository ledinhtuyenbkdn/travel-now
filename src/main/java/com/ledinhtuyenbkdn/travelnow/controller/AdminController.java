package com.ledinhtuyenbkdn.travelnow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/admins")
    public String readAdmin() {
        return "admin role";
    }
}
