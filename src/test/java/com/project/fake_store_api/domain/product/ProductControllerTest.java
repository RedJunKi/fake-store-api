package com.project.fake_store_api.domain.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllProducts() throws Exception {
        List<Product> products = List.of(new Product(1L, "타이틀", 1000, Category.ELECTRONICS, "먼가", "이미지"));
        when(productService.findAll()).thenReturn(products);
        when(productService.findWithLimit(10L)).thenReturn(products);


        mockMvc.perform(get("/products")
                        .param("limit", "10")
                        .param("sort", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getProduct() throws Exception {
        Product product = new Product(1L, "타이틀", 1000, Category.ELECTRONICS, "설명", "이미지");
        when(productService.findOne(1L)).thenReturn(product);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getCategories() throws Exception {
        List<String> categories = Arrays.stream(Category.values())
                .map(Enum::toString)
                .toList();

        when(productService.getCategories()).thenReturn(categories);

        mockMvc.perform(get("/products/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getCategoryItem() throws Exception {
        List<Product> electronics = List.of(new Product(1L, "일렉트로닉스", 1000, Category.ELECTRONICS, "설명", "이미지"));

        when(productService.getDetailCategoryItem("ELECTRONICS")).thenReturn(electronics);

        mockMvc.perform(get("/products/categories/electronics"))
//                        .param("detailCategory", "electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void saveProduct() throws Exception {

        when(productService.saveProduct(new ProductDto())).thenReturn(new Product());
        mockMvc.perform(post("/products")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductDto())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void putProduct() throws Exception {
        Product product = new Product(1L, "타이틀", 1000, Category.ELECTRONICS, "설명", "이미지");
        when(productService.updateProduct(1L, new ProductDto())).thenReturn(product);

        mockMvc.perform(put("/products/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void patchProduct() throws Exception{
        Product product = new Product(1L, "타이틀", 1000, Category.ELECTRONICS, "설명", "이미지");
        when(productService.updateProduct(1L, new ProductDto())).thenReturn(product);

        mockMvc.perform(patch("/products/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteProduct() throws Exception {
        Product product = new Product(1L, "타이틀", 1000, Category.ELECTRONICS, "설명", "이미지");

        when(productService.deleteProduct(1L)).thenReturn(product);

        mockMvc.perform(delete("/products/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ProductDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}