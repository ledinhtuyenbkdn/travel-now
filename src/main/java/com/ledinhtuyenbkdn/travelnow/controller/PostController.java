package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.*;
import com.ledinhtuyenbkdn.travelnow.repository.*;
import com.ledinhtuyenbkdn.travelnow.response.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.response.SuccessfulResponse;
import com.ledinhtuyenbkdn.travelnow.service.StorageService;
import org.springframework.beans.support.PagedListHolder;
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
    public ResponseEntity readAllPosts(@PathVariable("touristId") Long id,
                                       @RequestParam(value = "page", defaultValue = "1") int page) {
        List<Post> posts = postRepository.findAllByTouristId(id);
        List<Map<String, Object>> responseJson = new ArrayList<>();
        posts.forEach(o -> {
            Map<String, Object> post = new LinkedHashMap<>();
            post.put("id", o.getId());
            post.put("content", o.getContent());
            post.put("images", o.getImages());
            post.put("place", o.getPlace());
            post.put("tourist", o.getTourist());
            post.put("createdAt", o.getCreatedAt());
            post.put("updatedAt", o.getUpdatedAt());
            post.put("likes", likedPostRepository.findByPostId(o.getId()));
            responseJson.add(post);
        });
        //paginate
        PagedListHolder pages = new PagedListHolder(responseJson);
        pages.setPageSize(4); // number of items per page
        pages.setPage(page - 1);

        List<Place> result = page > pages.getPageCount() ? new ArrayList<>() : pages.getPageList();

        return new ResponseEntity(new SuccessfulResponse("success",
                result),
                HttpStatus.OK);
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
        if (!optionalPlace.isPresent() && postDTO.getPlaceId() != null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id place is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors), HttpStatus.BAD_REQUEST);
        }
        if (postDTO.getContent() != null) {
            post.setContent(postDTO.getContent());
        }
        if (postDTO.getPlaceId() != null) {
            post.setPlace(optionalPlace.get());
        }
        if (postDTO.getImages() != null && postDTO.getImages().length != 0) {
            //delete images
            post.getImages().forEach(o -> {
                storageService.delete(o.getUrl());
            });
            post.getImages().clear();
            //upload new image
            for (String image : postDTO.getImages()) {
                String imageUrl = storageService.store(image);
                post.getImages().add(new Image(imageUrl));
            }
        }
        post.setUpdatedAt(LocalDateTime.now());
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
