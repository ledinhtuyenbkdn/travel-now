package com.ledinhtuyenbkdn.travelnow.controllers;

import com.ledinhtuyenbkdn.travelnow.model.Photo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApiController {
    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public ResponseEntity<Photo> hello(@RequestBody Photo photo) {
        System.out.println(photo.getId());
        System.out.println(photo.getUrlImage());
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }
}
