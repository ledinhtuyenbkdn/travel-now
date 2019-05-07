package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.*;
import com.ledinhtuyenbkdn.travelnow.repository.*;
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

    private LikedPostRepository likedPostRepository;

    public PostController(PostRepository postRepository,
                          PlaceRepository placeRepository,
                          ImageRepository imageRepository,
                          StorageService storageService,
                          TouristRepository touristRepository,
                          LikedPostRepository likedPostRepository) {
        this.postRepository = postRepository;
        this.placeRepository = placeRepository;
        this.imageRepository = imageRepository;
        this.storageService = storageService;
        this.touristRepository = touristRepository;
        this.likedPostRepository = likedPostRepository;
    }

    @PostMapping("/posts")
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
        post.setUpdatedAt(LocalDateTime.now());
        post.setPlace(place);
        post.setTourist(tourist);

        for (String image : postDTO.getImages()) {
            String imageUrl = storageService.store(image);
            post.getImages().add(new Image(imageUrl));
        }
        postRepository.save(post);
        return new ResponseEntity(new SuccessfulResponse("success", post), HttpStatus.OK);
    }

    @GetMapping("/tourists/{touristId}/posts")
    public ResponseEntity readAllPosts(@PathVariable("touristId") Long id) {
        List<Post> posts = postRepository.findAllByTouristId(id);
        List<Map<String, Object>> responseJson = new ArrayList<>();
        posts.forEach(o -> {
            Map<String, Object> post = new LinkedHashMap<>();
            post.put("post", o);
            post.put("likes", likedPostRepository.findByPostId(o.getId()).stream().map(o1 -> {
                return o1.getTourist().getId();
            }).toArray(size -> new Long[size]));
            responseJson.add(post);
        });
        return new ResponseEntity(new SuccessfulResponse("success", responseJson), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity readPost(@PathVariable("postId") Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (!optionalPost.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Post post = optionalPost.get();
        return new ResponseEntity(new SuccessfulResponse("success", post), HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity updatePost(@PathVariable("postId") Long postId,
                                      @RequestBody PostDTO postDTO,
                                      Authentication authentication) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            return new ResponseEntity(new ErrorResponse("error", null),
                    HttpStatus.NOT_FOUND);
        }
        Post post = optionalPost.get();
        Tourist tourist = post.getTourist();
        if (!tourist.getUserName().equals(authentication.getPrincipal().toString())) {
            return new ResponseEntity(new ErrorResponse("error", null),
                    HttpStatus.FORBIDDEN);
        }
        Optional<Place> optionalPlace = placeRepository.findById(postDTO.getPlaceId());
        if (!optionalPlace.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id place is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors), HttpStatus.BAD_REQUEST);
        }
        post.setContent(postDTO.getContent());
        post.setPlace(optionalPlace.get());
        postRepository.save(post);
        return new ResponseEntity(new SuccessfulResponse("success", post), HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable("postId") Long postId,
                                     Authentication authentication) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            return new ResponseEntity(new ErrorResponse("error", null),
                    HttpStatus.NOT_FOUND);
        }
        Post post = optionalPost.get();
        Tourist tourist = post.getTourist();
        if (!tourist.getUserName().equals(authentication.getPrincipal().toString())) {
            return new ResponseEntity(new ErrorResponse("error", null),
                    HttpStatus.FORBIDDEN);
        }
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
