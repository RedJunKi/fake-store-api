package com.project.fake_store_api.domain.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductRepositoryImpl productRepositoryImpl;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findOne(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("없는 상품입니다."));
    }

    public List<Product> findWithLimit(Long limit) {
        return productRepositoryImpl.findWithLimit(limit);
    }
}
