package com.ledinhtuyenbkdn.travelnow.controllers;

import com.ledinhtuyenbkdn.travelnow.model.Tourist;
import com.ledinhtuyenbkdn.travelnow.repositories.TouristRepository;
import com.ledinhtuyenbkdn.travelnow.repositories.UserRepository;
import com.ledinhtuyenbkdn.travelnow.responses.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.responses.SuccessfulResponse;
import com.ledinhtuyenbkdn.travelnow.services.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity createTourist(@RequestBody Tourist tourist) {
        if (userRepository.findByUserName(tourist.getUserName()).isPresent()) {
            return new ResponseEntity(new ErrorResponse("error", "Username has existed."),
                    HttpStatus.BAD_REQUEST);
        } else {
            touristRepository.save(tourist);
            String fileExt = tourist.getAvatar()
                    .substring(tourist.getAvatar().lastIndexOf(".") + 1);
            String fileName = tourist.getId() + "." + fileExt;
            storageService.store(tourist.getAvatar(),
                    "avatar/" + fileName);
            tourist.setAvatar(fileName);
            touristRepository.save(tourist);
            return new ResponseEntity(new SuccessfulResponse("success", tourist),
                    HttpStatus.OK);
        }
    }
}
