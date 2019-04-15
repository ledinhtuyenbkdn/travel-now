package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.Province;
import com.ledinhtuyenbkdn.travelnow.repository.ProvinceRepository;
import com.ledinhtuyenbkdn.travelnow.response.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.response.SuccessfulResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ProvinceController {
    private ProvinceRepository provinceRepository;

    public ProvinceController(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    @PostMapping("/provinces")
    public ResponseEntity createProvince(@RequestBody Province province) {
        provinceRepository.save(province);
        return new ResponseEntity(new SuccessfulResponse("success", province), HttpStatus.OK);
    }

    @PutMapping("/provinces/{id}")
    public ResponseEntity updateProvince(@PathVariable("id") Long id, @RequestBody Province province) {
        Optional<Province> optionalProvince = provinceRepository.findById(id);
        if (!optionalProvince.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "ID is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Province currentProvince = optionalProvince.get();
        currentProvince.setNameProvince(province.getNameProvince());
        provinceRepository.save(currentProvince);
        return new ResponseEntity(new SuccessfulResponse("success", currentProvince), HttpStatus.OK);
    }

    @GetMapping("/provinces")
    public ResponseEntity readAllProvinces() {
        List<Province> provinces = new ArrayList<>();
        Iterable<Province> provinceIterable = provinceRepository.findAll();
        provinceIterable.forEach(o -> {
            provinces.add(o);
        });
        return new ResponseEntity(new SuccessfulResponse("success", provinces), HttpStatus.OK);
    }

    @DeleteMapping("/provinces/{id}")
    public ResponseEntity deleteProvince(@PathVariable("id") Long id) {
        Optional<Province> optionalProvince = provinceRepository.findById(id);
        if (!optionalProvince.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "ID is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        provinceRepository.deleteById(id);
        Map<String, String> messages = new HashMap<>();
        messages.put("delete", "delete successfully");
        return new ResponseEntity(new SuccessfulResponse("success", messages), HttpStatus.OK);
    }
}
