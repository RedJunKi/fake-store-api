package com.project.fake_store_api.domain.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllCarts() throws Exception {
        List<CartResponseDto> carts = List.of(new CartResponseDto(1L, 1L, new Date(), new ArrayList<>()), new CartResponseDto(2L, 2L, new Date(), new ArrayList<>()));
        when(cartService.findAll()).thenReturn(carts);
        when(cartService.findWithLimit(10L)).thenReturn(carts);

        mockMvc.perform(get("/carts")
                        .param("limit", "10")
                        .param("sort", "asc")
                        .param("startdate", "2024-07-01")
                        .param("enddate", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        mockMvc.perform(get("/carts")
                        .param("limit", "10")
                        .param("sort", "desc")
                        .param("startdate", "2024-07-01")
                        .param("enddate", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L));

    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getCart() throws Exception {
        CartResponseDto cart = new CartResponseDto(1L, 1L, new Date(), new ArrayList<>());
        when(cartService.findOne(1L)).thenReturn(cart);

        mockMvc.perform(get("/carts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getUserCart() throws Exception {
        List<CartResponseDto> carts = List.of(new CartResponseDto(1L, 1L, new Date(), new ArrayList<>()));
        when(cartService.findUserCarts(1L)).thenReturn(carts);

        mockMvc.perform(get("/carts/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void saveCart() throws Exception {

        CartDto cartDto = new CartDto(1L, new Date(), List.of(new CartItemDto(1L, 5), new CartItemDto(2L, 6)));
        CartResponseDto carts = new CartResponseDto(1L, 1L, new Date(), List.of(new CartItemDto(1L, 5), new CartItemDto(2L, 6)));
        when(cartService.updateCart(1L, cartDto)).thenReturn(carts);

        mockMvc.perform(post("/carts")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartDto)))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void putCart() throws Exception {
        CartDto cartDto = new CartDto();
        when(cartService.updateCart(anyLong(), any(CartDto.class))).thenReturn(new CartResponseDto());

        mockMvc.perform(put("/carts/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void patchCart() throws Exception {
        CartDto cartDto = new CartDto();
        when(cartService.updateCart(anyLong(), any(CartDto.class))).thenReturn(new CartResponseDto());

        mockMvc.perform(patch("/carts/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteCart() throws Exception {
        when(cartService.deleteCart(anyLong())).thenReturn(new CartResponseDto());

        mockMvc.perform(delete("/carts/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }
}