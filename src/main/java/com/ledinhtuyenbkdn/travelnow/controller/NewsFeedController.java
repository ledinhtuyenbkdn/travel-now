package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.Post;
import com.ledinhtuyenbkdn.travelnow.model.Tourist;
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
    public ResponseEntity getNewsFeed(@RequestParam(value = "page", defaultValue = "1") int page, Authentication authentication) {
        Optional<Tourist> optionalTourist = touristRepository.findByUserName(authentication.getPrincipal().toString());
        Tourist tourist = optionalTourist.get();
        List<Post> posts = postRepository.findNewsFeed(tourist.getId());
        //paginate
        PagedListHolder pages = new PagedListHolder(posts);
        pages.setPageSize(4); // number of items per page
        pages.setPage(page - 1);

        return new ResponseEntity(new SuccessfulResponse("success",
                pages.getPageList()),
                HttpStatus.OK);
    }
}
