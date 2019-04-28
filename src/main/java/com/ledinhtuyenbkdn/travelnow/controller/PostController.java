package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.*;
import com.ledinhtuyenbkdn.travelnow.repository.ImageRepository;
import com.ledinhtuyenbkdn.travelnow.repository.PlaceRepository;
import com.ledinhtuyenbkdn.travelnow.repository.PostRepository;
import com.ledinhtuyenbkdn.travelnow.repository.TouristRepository;
import com.ledinhtuyenbkdn.travelnow.response.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.response.SuccessfulResponse;
import com.ledinhtuyenbkdn.travelnow.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class PostController {

    private PostRepository postRepository;
    private PlaceRepository placeRepository;
    private ImageRepository imageRepository;
    private StorageService storageService;
    private TouristRepository touristRepository;

    public PostController(PostRepository postRepository,
                          PlaceRepository placeRepository,
                          ImageRepository imageRepository,
                          StorageService storageService,
                          TouristRepository touristRepository) {
        this.postRepository = postRepository;
        this.placeRepository = placeRepository;
        this.imageRepository = imageRepository;
        this.storageService = storageService;
        this.touristRepository = touristRepository;
    }

    @PostMapping("/tourists/{touristId}/posts")
    public ResponseEntity createPost(@Valid @RequestBody PostDTO postDTO,
                                     Authentication authentication) {
        Optional<Place> optionalPlace = placeRepository.findById(postDTO.getPlaceId());
        Optional<Tourist> optionalTourist = touristRepository.findByUserName(authentication.getPrincipal().toString());
        if (!optionalPlace.isPresent() || !optionalTourist.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id place is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors), HttpStatus.BAD_REQUEST);
        }

        Tourist tourist = optionalTourist.get();
        Place place = optionalPlace.get();

        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setPlace(place);
        post.setTourist(tourist);

        for (String image : postDTO.getImages()) {
            String imageUrl = storageService.store(image);
            post.getImages().add(new Image(imageUrl));
        }
        postRepository.save(post);

        post.getTourist().setUserName(null);
        post.getTourist().setPassword(null);
        return new ResponseEntity(new SuccessfulResponse("success", post), HttpStatus.OK);
    }

    @GetMapping("/tourists/{touristId}/posts")
    public ResponseEntity readAllPosts(@PathVariable("touristId") Long id) {
        List<Post> posts = postRepository.findAllByTouristId(id);

        return new ResponseEntity(new SuccessfulResponse("success", posts), HttpStatus.OK);
    }

    @GetMapping("/tourists/{touristId}/posts/{postId}")
    public ResponseEntity readPost(@PathVariable("postId") Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (!optionalPost.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Post post = optionalPost.get();
        post.getTourist().setUserName(null);
        post.getTourist().setPassword(null);
        return new ResponseEntity(new SuccessfulResponse("success", post), HttpStatus.OK);
    }

    @PutMapping("/tourists/{touristId}/posts/{postId}")
    public ResponseEntity updatePlace(@PathVariable("touristId") Long touristId,
                                      @PathVariable("postId") Long postId,
                                      @RequestBody PostDTO postDTO,
                                      Authentication authentication) {
        Optional<Tourist> optionalTourist = touristRepository.findById(touristId);
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent() || !optionalTourist.isPresent()) {
            return new ResponseEntity(new ErrorResponse("error", null),
                    HttpStatus.NOT_FOUND);
        }
        if (!optionalTourist.get().getUserName().equals(authentication.getPrincipal().toString())) {
            return new ResponseEntity(new ErrorResponse("error", null),
                    HttpStatus.FORBIDDEN);
        }
        Post post = optionalPost.get();
        postRepository.save(post);
        post.getTourist().setUserName(null);
        post.getTourist().setPassword(null);
        return new ResponseEntity(new SuccessfulResponse("success", post), HttpStatus.OK);
    }

    @DeleteMapping("/tourists/{touristId}/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable("touristId") Long touristId,
                                     @PathVariable("postId") Long postId,
                                     Authentication authentication) {
        Optional<Tourist> optionalTourist = touristRepository.findById(touristId);
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent() || !optionalTourist.isPresent()) {
            return new ResponseEntity(new ErrorResponse("error", null),
                    HttpStatus.NOT_FOUND);
        }
        if (!optionalTourist.get().getUserName().equals(authentication.getPrincipal().toString())) {
            return new ResponseEntity(new ErrorResponse("error", null),
                    HttpStatus.FORBIDDEN);
        }
        Post post = optionalPost.get();
        for (Image image : post.getImages()) {
            storageService.delete(image.getUrl());
            imageRepository.delete(image);
        }
        postRepository.delete(post);
        Map<String, String> messages = new HashMap<>();
        messages.put("delete", "delete successfully");
        return new ResponseEntity(new SuccessfulResponse("success", messages), HttpStatus.OK);
    }
}
