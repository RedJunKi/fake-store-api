package com.project.fake_store_api.domain.product;

import java.util.Comparator;

import com.project.fake_store_api.global.annotation.Retry;
import com.project.fake_store_api.global.annotation.Trace;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "제품 API")
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private static int seq = 0;

    @Trace
    @Retry
    @GetMapping
    @Operation(summary = "모든 제품 정보 검색", description = "필터링 및 정렬(선택사항)을 사용하여 모든 제품 목록 검색")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(value = "limit", required = false) Long limit,
                                                       @RequestParam(value = "sort", required = false) String condition) {

        seq++;

        if (seq % 2 == 0) {
            throw new IllegalArgumentException("Retry 예외 발생");
        }
        List<Product> result = fetchProducts(limit);
        result = sortProducts(result ,condition);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "제품 정보 검색", description = "제품 ID를 사용하여 제품 검색")
    public ResponseEntity<Product> getProduct(@PathVariable("productId") Long productId) {
        try {
            Product result = productService.findOne(productId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/categories")
    @Operation(summary = "카테고리 목록 검색", description = "모든 카테고리 목록 가져오기")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = productService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{detailCategory}")
    @Operation(summary = "카테고리 별 검색", description = "카테고리 별 제품 목록 가져오기")
    public ResponseEntity<List<Product>> getCategoryItem(@PathVariable("detailCategory") String detailCategory) {
        List<Product> result = productService.getDetailCategoryItem(detailCategory);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "제품 등록", description = "새로운 제품 정보 등록")
    public ResponseEntity<Product> saveProduct(@Validated @RequestBody ProductDto productDto) {
        Product result = productService.saveProduct(productDto);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "제품 정보 업데이트", description = "제품 ID로 검색된 제품 정보 업데이트")
    public ResponseEntity<Product> putProduct(@PathVariable("productId") Long productId, @Validated @RequestBody ProductDto productDto) {
        Product result = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{productId}")
    @Operation(summary = "제품 정보 부분적 업데이트", description = "제품 ID로 검색된 제품 정보 부분적 업데이트")
    public ResponseEntity<Product> patchProduct(@PathVariable("productId") Long productId, @Validated @RequestBody ProductDto productDto) {
        Product result = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "제품 삭제", description = "제품 ID로 검색된 제품 정보 삭제")
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
