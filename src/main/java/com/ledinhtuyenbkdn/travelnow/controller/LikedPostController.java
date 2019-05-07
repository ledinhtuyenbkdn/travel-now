package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.LikedPost;
import com.ledinhtuyenbkdn.travelnow.model.LikedPostDTO;
import com.ledinhtuyenbkdn.travelnow.model.Post;
import com.ledinhtuyenbkdn.travelnow.model.Tourist;
import com.ledinhtuyenbkdn.travelnow.repository.LikedPostRepository;
import com.ledinhtuyenbkdn.travelnow.repository.PostRepository;
import com.ledinhtuyenbkdn.travelnow.repository.TouristRepository;
import com.ledinhtuyenbkdn.travelnow.response.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.response.SuccessfulResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class LikedPostController {
    private TouristRepository touristRepository;
    private PostRepository postRepository;
    private LikedPostRepository likedPostRepository;

    public LikedPostController(TouristRepository touristRepository,
                               PostRepository postRepository,
                               LikedPostRepository likedPostRepository) {
        this.touristRepository = touristRepository;
        this.postRepository = postRepository;
        this.likedPostRepository = likedPostRepository;
    }

    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity createLikedPost(@PathVariable("postId") Long postId,
                                          Authentication authentication) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id place is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        String username = authentication.getPrincipal().toString();
        Tourist tourist = touristRepository.findByUserName(username).get();
        //check if tourist has rated this place
        if (!likedPostRepository.findByPostIdAndTouristId(postId, tourist.getId()).isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("rate", "you have rated this place");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.BAD_REQUEST);
        }
        //create new like
        Post post = optionalPost.get();

        LikedPost likedPost = new LikedPost();
        likedPost.setTourist(tourist);
        likedPost.setPost(post);
        likedPostRepository.save(likedPost);

        LikedPostDTO likedPostDTO = new LikedPostDTO();
        likedPostDTO.setId(likedPost.getId());
        likedPostDTO.setTouristId(likedPost.getTourist().getId());
        return new ResponseEntity(new SuccessfulResponse("success", likedPostDTO), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity readAllLikedPosts(@PathVariable("postId") Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id place is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Post post = optionalPost.get();
        List<LikedPost> allLikes = likedPostRepository.findByPostId(post.getId());
        List<LikedPostDTO> allLikeDTO = new ArrayList<>();
        for (LikedPost likedPost : allLikes) {
            LikedPostDTO likedPostDTO = new LikedPostDTO();
            likedPostDTO.setId(likedPost.getId());
            likedPostDTO.setTouristId(likedPost.getTourist().getId());
            allLikeDTO.add(likedPostDTO);
        }
        return new ResponseEntity(new SuccessfulResponse("success", allLikeDTO),
                HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/likes")
    public ResponseEntity deleteLikedPost(@PathVariable("postId") Long postId,
                                          Authentication authentication) {
        Tourist tourist = touristRepository.findByUserName(authentication.getPrincipal().toString()).get();
        Optional<LikedPost> optionalLikedPost = likedPostRepository.findByPostIdAndTouristId(postId, tourist.getId());
        if (!optionalLikedPost.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "like is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        LikedPost likedPost = optionalLikedPost.get();
        likedPostRepository.delete(likedPost);
        return new ResponseEntity(new SuccessfulResponse("success", "deleted"),
                HttpStatus.OK);
    }
}
