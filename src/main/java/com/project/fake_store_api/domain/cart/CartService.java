package com.project.fake_store_api.domain.cart;

import com.project.fake_store_api.domain.product.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartRepositoryImpl cartRepositoryImpl;

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    public List<Cart> findWithLimit(Long limit) {
        return cartRepositoryImpl.findWithLimit(limit);
    }

    public Cart findOne(Long cartId) {
        return findById(cartId);
    }

    private Cart findById(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException("해당 카트가 없습니다."));
    }

    public Cart save(CartDto cartDto) {
        Cart cart = new Cart();
//        cart.setUser();
        cart.setCreatedAt(cartDto.getDate());
        cart.setCartItems(cartDto.getCartItem());
        return cart;
    }
}
