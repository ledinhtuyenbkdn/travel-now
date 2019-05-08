package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.Place;
import com.ledinhtuyenbkdn.travelnow.model.RateDTO;
import com.ledinhtuyenbkdn.travelnow.model.RatedPlace;
import com.ledinhtuyenbkdn.travelnow.model.Tourist;
import com.ledinhtuyenbkdn.travelnow.repository.PlaceRepository;
import com.ledinhtuyenbkdn.travelnow.repository.RatedPlaceRepository;
import com.ledinhtuyenbkdn.travelnow.repository.TouristRepository;
import com.ledinhtuyenbkdn.travelnow.response.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.response.SuccessfulResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class RatedPlaceController {
    private TouristRepository touristRepository;
    private PlaceRepository placeRepository;
    private RatedPlaceRepository ratedPlaceRepository;

    public RatedPlaceController(TouristRepository touristRepository,
                                PlaceRepository placeRepository,
                                RatedPlaceRepository ratedPlaceRepository) {
        this.touristRepository = touristRepository;
        this.placeRepository = placeRepository;
        this.ratedPlaceRepository = ratedPlaceRepository;
    }

    @PostMapping("/places/{idPlace}/rates")
    public ResponseEntity createRatePlace(@PathVariable("idPlace") Long idPlace,
                                          @Valid @RequestBody RatedPlace ratedPlace,
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
        //check if tourist has rated this place
        if (ratedPlaceRepository.findByPlaceIdAndTouristId(idPlace, tourist.getId()).isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("rate", "you have rated this place");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.BAD_REQUEST);
        }
        //create new rate
        Place place = optionalPlace.get();

        ratedPlace.setTourist(tourist);
        ratedPlace.setPlace(place);
        ratedPlaceRepository.save(ratedPlace);
        //convert to DTO
        RateDTO rateDTO = new RateDTO();
        rateDTO.setId(ratedPlace.getId());
        rateDTO.setNumberStar(ratedPlace.getNumberStar());
        rateDTO.setTouristId(ratedPlace.getTourist().getId());
        return new ResponseEntity(new SuccessfulResponse("success", rateDTO), HttpStatus.OK);
    }

    @GetMapping("/places/{idPlace}/rates")
    public ResponseEntity readAllRates(@PathVariable("idPlace") Long idPlace) {
        Optional<Place> optionalPlace = placeRepository.findById(idPlace);
        if (!optionalPlace.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id place is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Place place = optionalPlace.get();
        List<RatedPlace> allRates = ratedPlaceRepository.findByPlaceId(place.getId());
        List<RateDTO> allRateDTO = new ArrayList<>();
        for (RatedPlace rate : allRates) {
            RateDTO rateDTO = new RateDTO();
            rateDTO.setId(rate.getId());
            rateDTO.setNumberStar(rate.getNumberStar());
            rateDTO.setTouristId(rate.getTourist().getId());
            allRateDTO.add(rateDTO);
        }
        return new ResponseEntity(new SuccessfulResponse("success", allRateDTO),
                HttpStatus.OK);
    }

    @PutMapping("/places/{idPlace}/rates/{idRate}")
    public ResponseEntity updateRatePlace(@PathVariable("idRate") Long idRate,
                                          @RequestBody RatedPlace newRatedPlace,
                                          Authentication authentication) {
        Optional<RatedPlace> optionalRatedPlace = ratedPlaceRepository.findById(idRate);
        if (!optionalRatedPlace.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id comment is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        RatedPlace ratedPlace = optionalRatedPlace.get();
        Tourist tourist = ratedPlace.getTourist();
        if (authentication.getPrincipal().toString().equals(tourist.getUserName())) {
            ratedPlace.setNumberStar(newRatedPlace.getNumberStar());
            ratedPlaceRepository.save(ratedPlace);
            //convert to DTO
            RateDTO rateDTO = new RateDTO();
            rateDTO.setId(ratedPlace.getId());
            rateDTO.setNumberStar(ratedPlace.getNumberStar());
            rateDTO.setTouristId(ratedPlace.getTourist().getId());
            return new ResponseEntity(new SuccessfulResponse("success", rateDTO),
                    HttpStatus.OK);
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("authorization", "you aren't allowed to update this comment.");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.FORBIDDEN);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity(new ErrorResponse("error", errors),
                HttpStatus.BAD_REQUEST);
    }
}
