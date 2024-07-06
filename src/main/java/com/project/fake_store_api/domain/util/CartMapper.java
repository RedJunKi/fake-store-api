package com.project.fake_store_api.domain.util;

import com.project.fake_store_api.domain.cart.Cart;
import com.project.fake_store_api.domain.cart.CartItemDto;
import com.project.fake_store_api.domain.cart.CartResponseDto;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartResponseDto toDto(Cart cart) {
        CartResponseDto result = new CartResponseDto();

        createCartResponseDto(cart, result);

        return result;
    }

    private static void createCartResponseDto(Cart cart, CartResponseDto result) {
        result.setId(cart.getId());
        result.setUserId(cart.getUser().getId());
        result.setDate(cart.getCreatedAt());

        List<CartItemDto> cartItemDtoList = cart.getCartItems().stream()
                .map(cartItem -> {
                    CartItemDto cartItemDto = new CartItemDto();
                    cartItemDto.setProductId(cartItem.getProduct().getId());
                    cartItemDto.setQuantity(cartItem.getQuantity());

                    return cartItemDto;
                })
                .collect(Collectors.toList());

        result.setCartItems(cartItemDtoList);
    }
}
