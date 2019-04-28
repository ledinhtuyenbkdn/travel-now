package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.CommentDTO;
import com.ledinhtuyenbkdn.travelnow.model.CommentedPlace;
import com.ledinhtuyenbkdn.travelnow.model.Place;
import com.ledinhtuyenbkdn.travelnow.model.Tourist;
import com.ledinhtuyenbkdn.travelnow.repository.CommentedPlaceRepository;
import com.ledinhtuyenbkdn.travelnow.repository.PlaceRepository;
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
public class CommentedPlaceController {
    private TouristRepository touristRepository;
    private PlaceRepository placeRepository;
    private CommentedPlaceRepository commentedPlaceRepository;

    public CommentedPlaceController(TouristRepository touristRepository,
                                    PlaceRepository placeRepository,
                                    CommentedPlaceRepository commentedPlaceRepository) {
        this.touristRepository = touristRepository;
        this.placeRepository = placeRepository;
        this.commentedPlaceRepository = commentedPlaceRepository;
    }

    @PostMapping("/places/{idPlace}/comments")
    public ResponseEntity createCommentPlace(@PathVariable("idPlace") Long idPlace,
                                             @RequestBody CommentedPlace commentedPlace,
                                             Authentication authentication) {
        Optional<Place> optionalPlace = placeRepository.findById(idPlace);
        if (!optionalPlace.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id place is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        String username = authentication.getPrincipal().toString();
        Tourist tourist = touristRepository.findByUserName(username).get();
        Place place = optionalPlace.get();

        commentedPlace.setCreatedAt(LocalDateTime.now());
        commentedPlace.setTourist(tourist);
        commentedPlace.setPlace(place);
        commentedPlaceRepository.save(commentedPlace);
        return new ResponseEntity(new SuccessfulResponse("success", commentedPlace), HttpStatus.OK);
    }

    @GetMapping("/places/{idPlace}/comments")
    public ResponseEntity readAllComments(@PathVariable("idPlace") Long idPlace) {
        Optional<Place> optionalPlace = placeRepository.findById(idPlace);
        if (!optionalPlace.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id place is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Place place = optionalPlace.get();
        List<CommentedPlace> allCmts = commentedPlaceRepository.findByPlaceId(place.getId());
        return new ResponseEntity(new SuccessfulResponse("success", allCmts),
                HttpStatus.OK);
    }

    @PutMapping("/places/{idPlace}/comments/{idComment}")
    public ResponseEntity updateCommentPlace(@PathVariable("idComment") Long idCmt,
                                             @RequestBody CommentedPlace newCommentPlace,
                                             Authentication authentication) {
        Optional<CommentedPlace> optionalCommentedPlace = commentedPlaceRepository.findById(idCmt);
        if (!optionalCommentedPlace.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id comment is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        CommentedPlace commentedPlace = optionalCommentedPlace.get();
        Tourist tourist = commentedPlace.getTourist();
        if (authentication.getPrincipal().toString().equals(tourist.getUserName())) {
            commentedPlace.setContent(newCommentPlace.getContent());
            commentedPlaceRepository.save(commentedPlace);
            return new ResponseEntity(new SuccessfulResponse("success", commentedPlace),
                    HttpStatus.OK);
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("authorization", "you aren't allowed to update this comment.");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/places/{idPlace}/comments/{idComment}")
    public ResponseEntity deleteCommentPlace(@PathVariable("idComment") Long idCmt,
                                             Authentication authentication) {
        Optional<CommentedPlace> optionalCommentedPlace = commentedPlaceRepository.findById(idCmt);
        if (!optionalCommentedPlace.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id comment is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        CommentedPlace commentedPlace = optionalCommentedPlace.get();
        Tourist tourist = commentedPlace.getTourist();
        if (authentication.getPrincipal().toString().equals(tourist.getUserName())) {
            commentedPlaceRepository.delete(commentedPlace);
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
