package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.Image;
import com.ledinhtuyenbkdn.travelnow.model.Tourist;
import com.ledinhtuyenbkdn.travelnow.model.TouristDTO;
import com.ledinhtuyenbkdn.travelnow.repository.ImageRepository;
import com.ledinhtuyenbkdn.travelnow.repository.TouristRepository;
import com.ledinhtuyenbkdn.travelnow.repository.UserRepository;
import com.ledinhtuyenbkdn.travelnow.response.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.response.SuccessfulResponse;
import com.ledinhtuyenbkdn.travelnow.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class TouristController {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private TouristRepository touristRepository;
    private ImageRepository imageRepository;
    private StorageService storageService;

    public TouristController(PasswordEncoder passwordEncoder,
                             UserRepository userRepository,
                             TouristRepository touristRepository,
                             ImageRepository imageRepository,
                             StorageService storageService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.touristRepository = touristRepository;
        this.imageRepository = imageRepository;
        this.storageService = storageService;
    }

    @PostMapping("/tourists")
    public ResponseEntity createTourist(@Valid @RequestBody TouristDTO touristDTO) {
        if (userRepository.findByUserName(touristDTO.getUserName()).isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("userName", "username has existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.BAD_REQUEST);
        } else {
            Tourist tourist = new Tourist();
            tourist.setUserName(touristDTO.getUserName());
            tourist.setFullName(touristDTO.getFullName());
            //set default image avatar
            Image image = new Image();
            image.setUrl("http://res.cloudinary.com/ledinhtuyenbkdn/image/upload/default");
            tourist.setImage(image);

            tourist.setRole("ROLE_TOURIST");
            tourist.setPassword(passwordEncoder.encode(touristDTO.getPassword()));
            touristRepository.save(tourist);
            return new ResponseEntity(new SuccessfulResponse("success", tourist),
                    HttpStatus.OK);
        }
    }

    @GetMapping("/tourists/{id}")
    public ResponseEntity readTourist(@PathVariable("id") Long id) {
        Optional<Tourist> optionalTourist = touristRepository.findById(id);
        //check valid id tourist
        if (!optionalTourist.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "ID is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Tourist tourist = optionalTourist.get();

        return new ResponseEntity(new SuccessfulResponse("success", tourist),
                HttpStatus.OK);
    }

    @GetMapping("/tourists")
    public ResponseEntity getAllTourist(@RequestParam(value = "s", defaultValue = "") String keyword) {
        List<Tourist> tourists = touristRepository.findAllByFullNameContains(keyword);
        return ResponseEntity.ok(tourists);
    }

    @PutMapping("/tourists/{id}")
    public ResponseEntity updateTourist(@PathVariable("id") Long id,
                                        @Valid @RequestBody TouristDTO touristDTO,
                                        Authentication authentication) {
        Optional<Tourist> optionalTourist = touristRepository.findById(id);
        //check valid id tourist
        if (!optionalTourist.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "ID is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Tourist currentTourist = optionalTourist.get();
        if (authentication.getPrincipal().toString().equals(currentTourist.getUserName())) {
            currentTourist.setPassword(passwordEncoder.encode(touristDTO.getPassword()));
            currentTourist.setFullName(touristDTO.getFullName());
            currentTourist.setGender(touristDTO.getGender());
            currentTourist.setBirthDate(touristDTO.getBirthDate());
            currentTourist.setNationality(touristDTO.getNationality());
            touristRepository.save(currentTourist);

            return new ResponseEntity(new SuccessfulResponse("success", currentTourist),
                    HttpStatus.OK);
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("authorization", "you aren't allowed to update this tourist.");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/tourists/{id}/avatar")
    public ResponseEntity updateAvatar(@PathVariable("id") Long id,
                                       @RequestBody Image image,
                                       Authentication authentication) {
        Optional<Tourist> optionalTourist = touristRepository.findById(id);
        //check valid id tourist
        if (!optionalTourist.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "ID is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Tourist currentTourist = optionalTourist.get();
        if (authentication.getPrincipal().toString().equals(currentTourist.getUserName())) {
            Image currentImage = currentTourist.getImage();
            if (!currentImage.getUrl().equals("http://res.cloudinary.com/ledinhtuyenbkdn/image/upload/default")) {
                storageService.delete(currentImage.getUrl());
            }
            String newImageUrl = storageService.store(image.getUrl());
            currentImage.setUrl(newImageUrl);
            imageRepository.save(currentImage);
            return new ResponseEntity(new SuccessfulResponse("success", currentTourist),
                    HttpStatus.OK);
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("authorization", "you aren't allowed to update this tourist.");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.FORBIDDEN);
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
