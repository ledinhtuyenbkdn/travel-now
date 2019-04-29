package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.*;
import com.ledinhtuyenbkdn.travelnow.repository.*;
import com.ledinhtuyenbkdn.travelnow.response.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.response.SuccessfulResponse;
import com.ledinhtuyenbkdn.travelnow.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class PlaceController {
    private PlaceRepository placeRepository;
    private ProvinceRepository provinceRepository;
    private CategoryRepository categoryRepository;
    private ImageRepository imageRepository;
    private StorageService storageService;
    private RatedPlaceRepository ratedPlaceRepository;

    public PlaceController(PlaceRepository placeRepository,
                           ProvinceRepository provinceRepository,
                           CategoryRepository categoryRepository,
                           ImageRepository imageRepository,
                           StorageService storageService,
                           RatedPlaceRepository ratedPlaceRepository) {
        this.placeRepository = placeRepository;
        this.provinceRepository = provinceRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.storageService = storageService;
        this.ratedPlaceRepository = ratedPlaceRepository;
    }

    @PostMapping("/places")
    public ResponseEntity createPlace(@Valid @RequestBody PlaceDTO placeDTO) {
        Optional<Province> optionalProvince = provinceRepository.findById(Long.valueOf(placeDTO.getProvinceId()));
        Optional<Category> optionalCategory = categoryRepository.findById(Long.valueOf(placeDTO.getCategoryId()));
        Map<String, String> errors = new HashMap<>();

        if (optionalCategory.isPresent() && optionalCategory.isPresent()) {
            Place place = new Place();
            place.setNamePlace(placeDTO.getNamePlace());
            place.setAbout(placeDTO.getAbout());
            place.setAddress(placeDTO.getAddress());
            place.setLatitude(placeDTO.getLatitude());
            place.setLongitude(placeDTO.getLongitude());
            place.setProvince(optionalProvince.get());
            place.setCategory(optionalCategory.get());
            place.setCreatedAt(LocalDateTime.now());
            place.setUpdatedAt(LocalDateTime.now());
            for (String image : placeDTO.getImages()) {
                String imageUrl = storageService.store(image);
                place.getImages().add(new Image(imageUrl));
            }
            placeRepository.save(place);
            return new ResponseEntity(new SuccessfulResponse("success", place), HttpStatus.OK);
        } else {
            if (!optionalCategory.isPresent()) {
                errors.put("category", "id is not exist");
            }
            if (!optionalProvince.isPresent()) {
                errors.put("province", "id is not exist");
            }
        }
        return new ResponseEntity(new ErrorResponse("error", errors), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/places")
    public ResponseEntity getAllPlaces(@RequestParam(value = "s", defaultValue = "") String keyword) {
        List<Place> places = placeRepository.findAllByNamePlaceContains(keyword);
        List<Map<String, Object>> responseJson = new ArrayList<>();
        places.forEach(place -> {
            List<RatedPlace> rates = ratedPlaceRepository.findByPlaceId(place.getId());
            int numRates = rates.size();
            int sumAllStars = rates.stream().reduce(0, (sum, rate) -> sum + rate.getNumberStar(), Integer::sum);
            double averageStar = numRates == 0 ? 0 : sumAllStars * 1.0 / numRates;
            Map<String, Object> json = new LinkedHashMap<>();
            json.put("id", place.getId());
            json.put("namePlace", place.getNamePlace());
            json.put("about", place.getAbout());
            json.put("address", place.getAddress());
            json.put("latitude", place.getLatitude());
            json.put("longitude", place.getLongitude());
            json.put("images", place.getImages());
            json.put("province", place.getProvince());
            json.put("category", place.getCategory());
            json.put("createdAt", place.getCreatedAt());
            json.put("updatedAt", place.getUpdatedAt());
            json.put("averageStar", averageStar);
            responseJson.add(json);
        });
        return ResponseEntity.ok(responseJson);
    }

    @GetMapping("/places/{id}")
    public ResponseEntity readPlace(@PathVariable("id") Long id) {
        Optional<Place> optionalPlace = placeRepository.findById(id);
        if (!optionalPlace.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Place place = optionalPlace.get();
        List<RatedPlace> rates = ratedPlaceRepository.findByPlaceId(id);
        int numRates = rates.size();
        int sumAllStars = rates.stream().reduce(0, (sum, rate) -> sum + rate.getNumberStar(), Integer::sum);
        double averageStar = numRates == 0 ? 0 : sumAllStars * 1.0 / numRates;
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("id", place.getId());
        json.put("namePlace", place.getNamePlace());
        json.put("about", place.getAbout());
        json.put("address", place.getAddress());
        json.put("latitude", place.getLatitude());
        json.put("longitude", place.getLongitude());
        json.put("images", place.getImages());
        json.put("province", place.getProvince());
        json.put("category", place.getCategory());
        json.put("createdAt", place.getCreatedAt());
        json.put("updatedAt", place.getUpdatedAt());
        json.put("averageStar", averageStar);
        return new ResponseEntity(new SuccessfulResponse("success", json), HttpStatus.OK);
    }

    @PutMapping("/places/{id}")
    public ResponseEntity updatePlace(@PathVariable("id") Long id, @RequestBody PlaceDTO placeDTO) {
        Optional<Province> optionalProvince = provinceRepository.findById(Long.valueOf(placeDTO.getProvinceId()));
        Optional<Category> optionalCategory = categoryRepository.findById(Long.valueOf(placeDTO.getCategoryId()));
        Optional<Place> optionalPlace = placeRepository.findById(id);

        if (!optionalPlace.isPresent() || !optionalCategory.isPresent() || !optionalProvince.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            if (!optionalPlace.isPresent()) {
                errors.put("id", "id is not exist");
            }
            if (!optionalCategory.isPresent()) {
                errors.put("category", "id is not exist");
            }
            if (!optionalProvince.isPresent()) {
                errors.put("province", "id is not exist");
            }
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Place place = optionalPlace.get();
        place.setNamePlace(placeDTO.getNamePlace());
        place.setAbout(placeDTO.getAbout());
        place.setAddress(placeDTO.getAddress());
        place.setLatitude(placeDTO.getLatitude());
        place.setLongitude(placeDTO.getLongitude());
        place.setProvince(optionalProvince.get());
        place.setCategory(optionalCategory.get());
        place.setUpdatedAt(LocalDateTime.now());
        placeRepository.save(place);

        return new ResponseEntity(new SuccessfulResponse("success", place), HttpStatus.OK);
    }

    @DeleteMapping("/places/{id}")
    public ResponseEntity deletePlace(@PathVariable("id") Long id) {
        Optional<Place> optionalPlace = placeRepository.findById(id);
        if (!optionalPlace.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "id is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Place place = optionalPlace.get();
        for (Image image : place.getImages()) {
            storageService.delete(image.getUrl());
            imageRepository.delete(image);
        }

        placeRepository.delete(place);

        Map<String, String> messages = new HashMap<>();
        messages.put("delete", "delete successfully");
        return new ResponseEntity(new SuccessfulResponse("success", messages), HttpStatus.OK);
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
