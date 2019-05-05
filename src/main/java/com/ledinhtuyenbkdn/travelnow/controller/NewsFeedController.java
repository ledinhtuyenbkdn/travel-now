package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.Tourist;
import com.ledinhtuyenbkdn.travelnow.repository.PostRepository;
import com.ledinhtuyenbkdn.travelnow.repository.TouristRepository;
import com.ledinhtuyenbkdn.travelnow.response.SuccessfulResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class NewsFeedController {
    private PostRepository postRepository;
    private TouristRepository touristRepository;

    public NewsFeedController(PostRepository postRepository, TouristRepository touristRepository) {
        this.postRepository = postRepository;
        this.touristRepository = touristRepository;
    }

    @GetMapping("/newsfeed")
    public ResponseEntity getNewsFeed(Authentication authentication) {
        Optional<Tourist> optionalTourist = touristRepository.findByUserName(authentication.getPrincipal().toString());
        Tourist tourist = optionalTourist.get();
        return new ResponseEntity(new SuccessfulResponse("success", postRepository.findNewsFeed(tourist.getId())),
                HttpStatus.OK);
    }
}
