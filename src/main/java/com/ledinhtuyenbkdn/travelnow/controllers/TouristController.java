package com.ledinhtuyenbkdn.travelnow.controllers;

import com.ledinhtuyenbkdn.travelnow.model.Tourist;
import com.ledinhtuyenbkdn.travelnow.repositories.TouristRepository;
import com.ledinhtuyenbkdn.travelnow.repositories.UserRepository;
import com.ledinhtuyenbkdn.travelnow.responses.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.responses.SuccessfulResponse;
import com.ledinhtuyenbkdn.travelnow.services.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TouristController {

    private UserRepository userRepository;
    private TouristRepository touristRepository;
    private StorageService storageService;

    public TouristController(UserRepository userRepository, TouristRepository touristRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.touristRepository = touristRepository;
        this.storageService = storageService;
    }

    @RequestMapping(value = "/tourists", method = RequestMethod.POST)
    public ResponseEntity createTourist(@Valid @RequestBody Tourist tourist) {
        if (userRepository.findByUserName(tourist.getUserName()).isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("userName", "username has existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.BAD_REQUEST);
        } else {
            tourist.setAvatar("default.jpg");
            touristRepository.save(tourist);
            return new ResponseEntity(new SuccessfulResponse("success", tourist),
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
