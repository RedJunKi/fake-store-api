package com.project.fake_store_api.domain.cart;

import jakarta.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long userId;
    private Date date;
    private List<CartItemDto> cartItems;
}
