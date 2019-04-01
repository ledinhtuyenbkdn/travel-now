package com.ledinhtuyenbkdn.travelnow.controllers;

import com.ledinhtuyenbkdn.travelnow.model.Tourist;
import com.ledinhtuyenbkdn.travelnow.repositories.TouristRepository;
import com.ledinhtuyenbkdn.travelnow.repositories.UserRepository;
import com.ledinhtuyenbkdn.travelnow.responses.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.responses.SuccessfulResponse;
import com.ledinhtuyenbkdn.travelnow.services.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class TouristController {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private TouristRepository touristRepository;
    private StorageService storageService;

    public TouristController(PasswordEncoder passwordEncoder, UserRepository userRepository, TouristRepository touristRepository, StorageService storageService) {
        this.passwordEncoder = passwordEncoder;
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
            tourist.setAvatar("avatar/default.png");
            tourist.setRole("ROLE_TOURIST");
            tourist.setPassword(passwordEncoder.encode(tourist.getPassword()));
            touristRepository.save(tourist);
            return new ResponseEntity(new SuccessfulResponse("success", tourist),
                    HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/tourists/{id}", method = RequestMethod.GET)
    public ResponseEntity readTourist(@PathVariable("id") String id) {
        Optional<Tourist> optionalTourist = touristRepository.findById(Long.parseLong(id));

        if (!optionalTourist.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "ID is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Tourist tourist = optionalTourist.get();
        tourist.setUserName(null);
        tourist.setPassword(null);
        tourist.setAvatar(storageService.load(tourist.getAvatar()));

        return new ResponseEntity(new SuccessfulResponse("success", tourist),
                HttpStatus.OK);
    }

//    @RequestMapping(value = "/tourists", method = RequestMethod.PUT)
//    public String updateTourist(@Valid @RequestBody Tourist tourist,
//                                Authentication authentication) {
//        String userName = authentication.getPrincipal().toString();
//        Tourist currentTourist = touristRepository.findByUserName(userName).get();
//
//        return "success";
//    }

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
