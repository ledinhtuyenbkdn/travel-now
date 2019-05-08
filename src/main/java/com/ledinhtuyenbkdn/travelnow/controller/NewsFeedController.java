package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.Place;
import com.ledinhtuyenbkdn.travelnow.model.Post;
import com.ledinhtuyenbkdn.travelnow.model.Tourist;
import com.ledinhtuyenbkdn.travelnow.repository.LikedPostRepository;
import com.ledinhtuyenbkdn.travelnow.repository.PostRepository;
import com.ledinhtuyenbkdn.travelnow.repository.TouristRepository;
import com.ledinhtuyenbkdn.travelnow.response.SuccessfulResponse;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class NewsFeedController {
    private PostRepository postRepository;
    private TouristRepository touristRepository;
    private LikedPostRepository likedPostRepository;

    public NewsFeedController(PostRepository postRepository, TouristRepository touristRepository, LikedPostRepository likedPostRepository) {
        this.postRepository = postRepository;
        this.touristRepository = touristRepository;
        this.likedPostRepository = likedPostRepository;
    }

    @GetMapping("/newsfeed")
    public ResponseEntity getNewsFeed(@RequestParam(value = "page", defaultValue = "1") int page, Authentication authentication) {
        Optional<Tourist> optionalTourist = touristRepository.findByUserName(authentication.getPrincipal().toString());
        Tourist tourist = optionalTourist.get();
        List<Post> posts = postRepository.findNewsFeed(tourist.getId());
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
}
