package com.project.fake_store_api.domain.cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.project.fake_store_api.domain.product.Product;
import com.project.fake_store_api.domain.product.ProductRepository;
import com.project.fake_store_api.domain.user.User;
import com.project.fake_store_api.domain.user.UserRepository;
import com.project.fake_store_api.domain.util.CartMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartRepositoryImpl cartRepositoryImpl;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    public List<CartResponseDto> findAll() {
        List<Cart> result = cartRepository.findAll();
        return result.stream()
                .map(CartMapper::toDto)
                .toList();
    }

    public List<CartResponseDto> findWithLimit(Long limit) {
        List<Cart> result = cartRepositoryImpl.findWithLimit(limit);

        return result.stream()
                .map(CartMapper::toDto)
                .toList();
    }

    public CartResponseDto findOne(Long cartId) {
        Cart cart = findById(cartId);
        return CartMapper.toDto(cart);
    }

    private Cart findById(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException("해당 카트가 없습니다."));
    }

    public CartResponseDto save(CartDto cartDto) {
        User user = userRepository.findById(cartDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(cartDto.getDate());

        List<CartItem> cartItems = createCartItems(cartDto, cart);

        cart.setCartItems(cartItems);
        cartRepository.save(cart);
        return CartMapper.toDto(cart);
    }

    public List<CartResponseDto> findUserCarts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        return user.getCarts().stream()
                .map(CartMapper::toDto)
                .toList();

    }

    public CartResponseDto updateCart(Long cartId, CartDto cartDto) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카트가 존재하지 않습니다."));

        List<CartItem> cartItems = cart.getCartItems();
        List<CartItemDto> updateItems = cartDto.getCartItems();

        Map<Long, CartItem> existCartItem = new HashMap<>();
        cartItems.forEach(i -> existCartItem.put(i.getProduct().getId(), i));

        for (CartItemDto cartItemDto : updateItems) {
            CartItem target = existCartItem.get(cartItemDto.getProductId());
            target.setQuantity(cartItemDto.getQuantity());
        }

        cart.setCreatedAt(cartDto.getDate());

        cartRepository.save(cart);
        return CartMapper.toDto(cart);
    }

    public CartResponseDto deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카트가 존재하지 않습니다."));
        cartRepository.deleteById(cartId);

        return CartMapper.toDto(cart);
    }

    private List<CartItem> createCartItems(CartDto cartDto, Cart cart) {
        return cartDto.getCartItems().stream()
                .map(cartItemDto -> {
                    Product product = productRepository.findById(cartItemDto.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

                    CartItem cartItem = new CartItem();
                    cartItem.setProduct(product);
                    cartItem.setQuantity(cartItemDto.getQuantity());
                    cartItem.setCart(cart);

                    return cartItem;
                }).collect(Collectors.toList());
    }
}
