package com.project.fake_store_api.domain.cart;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
@Slf4j
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartResponseDto>> getAllCarts(@RequestParam(value = "limit", required = false) Long limit,
                                                             @RequestParam(value = "sort", required = false) String condition,
                                                             @RequestParam(value = "startdate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                             @RequestParam(value = "enddate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        List<CartResponseDto> result = fetchCarts(limit);
        result = periodCarts(result, startDate, endDate);
        result = sortCarts(result, condition);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponseDto> getCart(@PathVariable("cartId") Long cartId) {
        CartResponseDto cart = cartService.findOne(cartId);

        return ResponseEntity.ok(cart);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartResponseDto>> getUserCart(@PathVariable("userId") Long userId) {
        List<CartResponseDto> carts = cartService.findUserCarts(userId);
        return ResponseEntity.ok(carts);
    }

    @PostMapping
    public ResponseEntity<CartResponseDto> saveCart(@RequestBody @Validated CartDto cartDto) {
        CartResponseDto cart = cartService.save(cartDto);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<CartResponseDto> putCart(@PathVariable("cartId") Long cartId, @RequestBody @Validated CartDto cartDto) {
        CartResponseDto cart = cartService.updateCart(cartId, cartDto);
        return ResponseEntity.ok(cart);
    }

    @PatchMapping("/{cartId}")
    public ResponseEntity<CartResponseDto> patchCart(@PathVariable("cartId") Long cartId, @RequestBody @Validated CartDto cartDto) {
        CartResponseDto cart = cartService.updateCart(cartId, cartDto);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<CartResponseDto> deleteCart(@PathVariable("cartId") Long cartId) {
        CartResponseDto cart = cartService.deleteCart(cartId);
        return ResponseEntity.ok(cart);
    }

    private List<CartResponseDto> fetchCarts(Long limit) {
        if (limit != null && limit > 0) {
            return cartService.findWithLimit(limit);
        } else {
            return cartService.findAll();
        }
    }

    private List<CartResponseDto> periodCarts(List<CartResponseDto> carts, Date startDate, Date endDate) {

        if (startDate != null && endDate != null) {
            return carts.stream()
                    .filter(c -> c.getDate().after(startDate))
                    .filter(c -> c.getDate().before(endDate))
                    .toList();
        }

        if (startDate != null) {
            return carts.stream()
                    .filter(c -> c.getDate().after(startDate))
                    .toList();
        }

        if (endDate != null) {
            return carts.stream()
                    .filter(c -> c.getDate().before(endDate))
                    .toList();
        }

        return carts;
    }

    private List<CartResponseDto> sortCarts(List<CartResponseDto> carts, String condition) {
        if (condition == null) {
            return carts;
        }
        log.info("condition={}", condition);

        if (condition.equals("asc")) {
            return carts.stream()
                    .sorted(Comparator.comparing(CartResponseDto::getId))
                    .toList();

        } else if (condition.equals("desc")) {
            return carts.stream()
                    .sorted(Comparator.comparing(CartResponseDto::getId).reversed())
                    .toList();
        } else {
            return carts;
        }
    }
}
