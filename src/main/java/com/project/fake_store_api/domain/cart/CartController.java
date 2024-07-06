package com.project.fake_store_api.domain.cart;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts(@RequestParam(value = "limit", required = false) Long limit,
                                                  @RequestParam(value = "sort", required = false) String condition,
                                                  @RequestParam(value = "startdate", required = false) Date startDate,
                                                  @RequestParam(value = "enddate", required = false) Date endDate) {

        List<Cart> result = fetchCarts(limit);
        result = periodCarts(result, startDate, endDate);
        result = sortCarts(result, condition);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable("cartId") Long cartId) {
        Cart cart = cartService.findOne(cartId);

        return ResponseEntity.ok(cart);
    }
//    user 구현 후 그래프 탐색 필요
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Cart>> getUserCart(@PathVariable("userId") Long userId) {
//
//        return ResponseEntity.ok();
//    }

    @PostMapping
    public ResponseEntity<Cart> saveCart(@RequestBody @Validated CartDto cartDto) {
        Cart cart = cartService.save(cartDto);
        return ResponseEntity.ok(cart);
    }

    private List<Cart> fetchCarts(Long limit) {
        if (limit != null && limit > 0) {
            return cartService.findWithLimit(limit);
        } else {
            return cartService.findAll();
        }
    }

    private List<Cart> periodCarts(List<Cart> carts, Date startDate, Date endDate) {

        if (startDate != null && endDate != null) {
            return carts.stream()
                    .filter(c -> c.getCreatedAt().after(startDate))
                    .filter(c -> c.getCreatedAt().before(endDate))
                    .toList();
        }

        if (startDate != null) {
            return carts.stream()
                    .filter(c -> c.getCreatedAt().after(startDate))
                    .toList();
        }

        if (endDate != null) {
            return carts.stream()
                    .filter(c -> c.getCreatedAt().before(endDate))
                    .toList();
        }

        return carts;
    }

    private List<Cart> sortCarts(List<Cart> carts, String condition) {
        if (condition == null) {
            return carts;
        }

        if (condition.equals("asc")) {
            return carts.stream()
                    .sorted(Comparator.comparing(Cart::getId))
                    .toList();

        } else if (condition.equals("desc")) {
            return carts.stream()
                    .sorted(Comparator.comparing(Cart::getId))
                    .toList();
        } else {
            return carts;
        }
    }
}
