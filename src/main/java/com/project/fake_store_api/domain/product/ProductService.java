package com.project.fake_store_api.domain.product;

import com.project.fake_store_api.global.error.BusinessLogicException;
import com.project.fake_store_api.global.error.ExceptionCode;
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
        return findById(productId);
    }

    public List<Product> findWithLimit(Long limit) {
        return productRepositoryImpl.findWithLimit(limit);
    }

    public List<String> getCategories() {
        List<Category> categories = productRepositoryImpl.findCategories();

        return categories.stream()
                .map(String::valueOf)
                .toList();
    }

    public List<Product> getDetailCategoryItem(String detailCategory) {
        return productRepositoryImpl.findProductWithCategory(detailCategory);
    }

    public Product saveProduct(ProductDto productDto) {
        Product product = new Product();
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getImage());

        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, ProductDto productDto) {
        Product findProduct = findById(productId);

        findProduct.setTitle(productDto.getTitle());
        findProduct.setPrice(productDto.getPrice());
        findProduct.setCategory(productDto.getCategory());
        findProduct.setDescription(productDto.getDescription());
        findProduct.setImage(productDto.getImage());

        return productRepository.save(findProduct);
    }

    public Product deleteProduct(Long productId) {
        Product result = findById(productId);
        productRepository.deleteById(productId);

        return result;
    }

    private Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ITEM_NOT_FOUND));
    }
}
