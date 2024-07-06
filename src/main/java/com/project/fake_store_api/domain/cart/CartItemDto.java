package com.project.fake_store_api.domain.cart;

import lombok.Data;

@Data
public class CartItemDto {

    private Long productId;
    private int quantity;
}
