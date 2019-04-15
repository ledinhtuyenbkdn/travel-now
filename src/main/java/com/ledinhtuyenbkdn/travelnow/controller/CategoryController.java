package com.ledinhtuyenbkdn.travelnow.controller;

import com.ledinhtuyenbkdn.travelnow.model.Category;
import com.ledinhtuyenbkdn.travelnow.repository.CategoryRepository;
import com.ledinhtuyenbkdn.travelnow.response.ErrorResponse;
import com.ledinhtuyenbkdn.travelnow.response.SuccessfulResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class CategoryController {
    private CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/categories")
    public ResponseEntity createCategory(@RequestBody Category category) {
        categoryRepository.save(category);
        return new ResponseEntity(new SuccessfulResponse("success", category), HttpStatus.OK);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "ID is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        Category currentCategory = optionalCategory.get();
        currentCategory.setNameCategory(category.getNameCategory());
        categoryRepository.save(currentCategory);
        return new ResponseEntity(new SuccessfulResponse("success", currentCategory), HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity readAllCategories() {
        List<Category> categories = new ArrayList<>();
        Iterable<Category> categoryIterable = categoryRepository.findAll();
        categoryIterable.forEach(o -> {
            categories.add(o);
        });
        return new ResponseEntity(new SuccessfulResponse("success", categories), HttpStatus.OK);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity deleteCategory(@PathVariable("id") Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "ID is not existed");
            return new ResponseEntity(new ErrorResponse("error", errors),
                    HttpStatus.NOT_FOUND);
        }
        categoryRepository.deleteById(id);
        Map<String, String> messages = new HashMap<>();
        messages.put("delete", "delete successfully");
        return new ResponseEntity(new SuccessfulResponse("success", messages), HttpStatus.OK);
    }
}
