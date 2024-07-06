package com.project.fake_store_api.domain.cart;

import jakarta.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class CartDto {

    private Long userId;
    private Date date;
    @NotEmpty
    private List<CartItem> cartItem;
}
