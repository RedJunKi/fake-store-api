package com.project.fake_store_api.domain.product;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
public class ProductDto {

    @NotEmpty
    private String title;
    @NotEmpty
    private int price;
    @NotEmpty
    private Category category;
    @NotEmpty
    private String description;
    @NotEmpty
    private String image;
}
