package com.ledinhtuyenbkdn.travelnow.controllers;

import com.ledinhtuyenbkdn.travelnow.model.Tourguide;
import com.ledinhtuyenbkdn.travelnow.repositories.TourguideRepository;
import com.ledinhtuyenbkdn.travelnow.repositories.UserRepository;
import com.ledinhtuyenbkdn.travelnow.responses.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.responses.SuccessfulResponse;
import com.ledinhtuyenbkdn.travelnow.services.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TourguideController {
    private UserRepository userRepository;
    private TourguideRepository tourguideRepository;
    private StorageService storageService;

    public TourguideController(UserRepository userRepository, TourguideRepository tourguideRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.tourguideRepository = tourguideRepository;
        this.storageService = storageService;
    }

    @RequestMapping(value = "/tourguides", method = RequestMethod.POST)
    public ResponseEntity createTourguide(@Valid @RequestBody Tourguide tourguide) {
        if (userRepository.findByUserName(tourguide.getUserName()).isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("userName", "username has existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.BAD_REQUEST);
        } else {
            tourguide.setAvatar("default.jpg");
            tourguide.setLicenceImage("none.jpg");
            tourguideRepository.save(tourguide);
            return new ResponseEntity(new SuccessfulResponse("success", tourguide),
                    HttpStatus.OK);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity(new ErrorResponse("error", errors),
                HttpStatus.BAD_REQUEST);
    }
}
