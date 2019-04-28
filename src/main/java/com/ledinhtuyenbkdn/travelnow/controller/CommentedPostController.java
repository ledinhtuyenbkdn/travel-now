package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.CommentDTO;
import com.ledinhtuyenbkdn.travelnow.model.CommentedPost;
import com.ledinhtuyenbkdn.travelnow.model.Post;
import com.ledinhtuyenbkdn.travelnow.model.Tourist;
import com.ledinhtuyenbkdn.travelnow.repository.CommentedPostRepository;
import com.ledinhtuyenbkdn.travelnow.repository.PostRepository;
import com.ledinhtuyenbkdn.travelnow.repository.TouristRepository;
import com.ledinhtuyenbkdn.travelnow.response.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.response.SuccessfulResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class CommentedPostController {
    private CommentedPostRepository commentedPostRepository;
    private TouristRepository touristRepository;
    private PostRepository postRepository;

    public CommentedPostController(CommentedPostRepository commentedPostRepository,
                                   TouristRepository touristRepository,
                                   PostRepository postRepository) {
        this.commentedPostRepository = commentedPostRepository;
        this.touristRepository = touristRepository;
        this.postRepository = postRepository;
    }

    @PostMapping("/tourists/{touristId}/posts/{postId}/comments")
    public ResponseEntity createCommentPost(@PathVariable("postId") Long postId,
                                            @RequestBody CommentedPost commentedPost,
                                            Authentication authentication) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id post is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        String username = authentication.getPrincipal().toString();
        Tourist tourist = touristRepository.findByUserName(username).get();
        Post post = optionalPost.get();

        commentedPost.setCreatedAt(LocalDateTime.now());
        commentedPost.setTourist(tourist);
        commentedPost.setPost(post);
        commentedPostRepository.save(commentedPost);
        return new ResponseEntity(new SuccessfulResponse("success", commentedPost), HttpStatus.OK);
    }

    @GetMapping("/tourists/{touristId}/posts/{postId}/comments")
    public ResponseEntity readAllComments(@PathVariable("postId") Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id place is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Post post = optionalPost.get();
        List<CommentedPost> allCmts = commentedPostRepository.findByPostId(post.getId());

        return new ResponseEntity(new SuccessfulResponse("success", allCmts),
                HttpStatus.OK);
    }

    @PutMapping("/tourists/{touristId}/posts/{postId}/comments/{idComment}")
    public ResponseEntity updateCommentPost(@PathVariable("idComment") Long idCmt,
                                            @RequestBody CommentedPost newCommentPost,
                                            Authentication authentication) {
        Optional<CommentedPost> optionalCommentedPost = commentedPostRepository.findById(idCmt);
        if (!optionalCommentedPost.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id comment is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        CommentedPost commentedPost = optionalCommentedPost.get();
        Tourist tourist = commentedPost.getTourist();
        if (authentication.getPrincipal().toString().equals(tourist.getUserName())) {
            commentedPost.setContent(newCommentPost.getContent());
            commentedPostRepository.save(commentedPost);
            return new ResponseEntity(new SuccessfulResponse("success", commentedPost),
                    HttpStatus.OK);
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("authorization", "you aren't allowed to update this comment.");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/tourists/{touristId}/posts/{postId}/comments/{idComment}")
    public ResponseEntity deleteCommentPost(@PathVariable("idComment") Long idCmt,
                                            Authentication authentication) {
        Optional<CommentedPost> optionalCommentedPost = commentedPostRepository.findById(idCmt);
        if (!optionalCommentedPost.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id comment is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        CommentedPost commentedPost = optionalCommentedPost.get();
        Tourist tourist = commentedPost.getTourist();
        if (authentication.getPrincipal().toString().equals(tourist.getUserName())) {
            commentedPostRepository.delete(commentedPost);
            Map<String, String> messages = new HashMap<>();
            messages.put("delete", "delete successfully");
            return new ResponseEntity(new SuccessfulResponse("success", messages), HttpStatus.OK);
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("authorization", "you aren't allowed to update this comment.");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.FORBIDDEN);
        }
    }
}
