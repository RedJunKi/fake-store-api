package com.project.fake_store_api.domain.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProduct(@RequestParam(value = "limit", required = false) Long limit) {
        List<Product> result;

        if (limit != null && limit > 0) {
            result = productService.findWithLimit(limit);
        } else {
            result = productService.findAll();
        }
        return ResponseEntity.ok(result);

    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable("productId") Long productId) {
        try {
            Product result = productService.findOne(productId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
