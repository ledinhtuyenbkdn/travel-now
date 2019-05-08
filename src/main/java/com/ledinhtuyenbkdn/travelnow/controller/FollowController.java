package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.Follow;
import com.ledinhtuyenbkdn.travelnow.model.FollowDTO;
import com.ledinhtuyenbkdn.travelnow.model.Tourist;
import com.ledinhtuyenbkdn.travelnow.repository.FollowRepository;
import com.ledinhtuyenbkdn.travelnow.repository.TouristRepository;
import com.ledinhtuyenbkdn.travelnow.response.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.response.SuccessfulResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class FollowController {
    private TouristRepository touristRepository;
    private FollowRepository followRepository;

    public FollowController(TouristRepository touristRepository,
                            FollowRepository followRepository) {
        this.touristRepository = touristRepository;
        this.followRepository = followRepository;
    }

    @PostMapping("/tourists/{id}/follows")
    public ResponseEntity followTourist(@PathVariable("id") Long followeeId, Authentication authentication) {
        Optional<Tourist> optionalFollowee = touristRepository.findById(followeeId);
        if (!optionalFollowee.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id tourist is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Tourist followee = optionalFollowee.get();
        Tourist follower = touristRepository.findByUserName(authentication.getPrincipal().toString()).get();

        if (followee.getId().equals(follower.getId())) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "can not follow yourself");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.BAD_REQUEST);
        }
        if (followRepository.findByFollowerIdAndFolloweeId(follower.getId(), followee.getId()).isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("follow", "you has been followed this tourist");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.BAD_REQUEST);
        }
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowee(followee);
        followRepository.save(follow);

        FollowDTO followDTO = new FollowDTO(follow.getId(), follower.getId(), followee.getId());
        return new ResponseEntity(new SuccessfulResponse("success", followDTO),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/tourists/{id}/follows")
    public ResponseEntity unfollowTourist(@PathVariable("id") Long followeeId, Authentication authentication) {
        Long followerId = touristRepository.findByUserName(authentication.getPrincipal().toString()).get().getId();
        Optional<Follow> optionalFollow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId);
        if (optionalFollow.isPresent()) {
            followRepository.delete(optionalFollow.get());
            return new ResponseEntity(new SuccessfulResponse("success", "deleted"),
                    HttpStatus.OK);
        }
        Map<String, String> errors = new HashMap<>();
        errors.put("follow", "you hasn't been followed this tourist");
        return new ResponseEntity(new ErrorResponse("error", errors),
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/tourists/{id}/follows")
    public ResponseEntity readAllFollowRelationship(@PathVariable("id") Long id) {
        Optional<Tourist> optionalTourist = touristRepository.findById(id);
        if (!optionalTourist.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id tourist is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        List<Tourist> allFollowers = followRepository.findByFolloweeId(id)
                .stream().map(o -> o.getFollower()).collect(Collectors.toList());
        List<Tourist> allFollowings = followRepository.findByFollowerId(id)
                .stream().map(o -> o.getFollowee()).collect(Collectors.toList());
        Map<String, Object> responseJson = new LinkedHashMap<>();
        responseJson.put("allFollowers", allFollowers);
        responseJson.put("allFollowings", allFollowings);
        return new ResponseEntity(new SuccessfulResponse("success", responseJson),
                HttpStatus.OK);
    }
}
