package com.project.fake_store_api.domain.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
public class ProductDto {

    private String title;
    private int price;

    private Category category;
    private String description;
    private String image;
}
