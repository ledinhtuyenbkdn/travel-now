package com.ledinhtuyenbkdn.travelnow.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @RequestMapping(value = "/admins", method = RequestMethod.GET)
    public String readAdmin() {
        return "admin role";
    }
}
