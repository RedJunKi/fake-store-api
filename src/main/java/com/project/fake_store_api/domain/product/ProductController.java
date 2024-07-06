package com.project.fake_store_api.domain.product;

import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(value = "limit", required = false) Long limit,
                                                       @RequestParam(value = "sort", required = false) String condition) {

        List<Product> result = fetchProducts(limit);
        result = sortProducts(result ,condition);

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

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = productService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{detailCategory}")
    public ResponseEntity<List<Product>> getCategoryItem(@PathVariable("detailCategory") String detailCategory) {
        List<Product> result = productService.getDetailCategoryItem(detailCategory);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@Validated @RequestBody ProductDto productDto) {
        Product result = productService.saveProduct(productDto);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> putProduct(@PathVariable("productId") Long productId, @Validated @RequestBody ProductDto productDto) {
        Product result = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<Product> patchProduct(@PathVariable("productId") Long productId, @Validated @RequestBody ProductDto productDto) {
        Product result = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("productId") Long productId) {
        Product result = productService.deleteProduct(productId);
        return ResponseEntity.ok(result);
    }

    private List<Product> sortProducts(List<Product> products, String condition) {
        if (condition == null) {
            return products;
        }

        if (condition.equals("asc")) {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getId))
                    .toList();

        } else if (condition.equals("desc")) {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getId).reversed())
                    .toList();
        } else {
            return products;
        }
    }

    private List<Product> fetchProducts(Long limit) {
        if (limit != null && limit > 0) {
            return productService.findWithLimit(limit);
        } else {
            return productService.findAll();
        }
    }
}
