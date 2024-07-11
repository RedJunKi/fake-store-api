package com.project.fake_store_api.domain.cart;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.project.fake_store_api.global.annotation.Trace;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
@Tag(name = "Cart", description = "카트 API")
@Slf4j
public class CartController {

    private final CartService cartService;

    @Trace
    @GetMapping
    @Operation(summary = "모든 카트 정보 가져오기", description = "필터링 및 정렬(선택사항)을 사용하여 모든 카트 목록 검색")
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
    @Operation(summary = "카트 ID로 카트 정보 검색", description = "카트 ID를 사용하여 카트 검색")
    public ResponseEntity<CartResponseDto> getCart(@PathVariable("cartId") Long cartId) {
        CartResponseDto cart = cartService.findOne(cartId);

        return ResponseEntity.ok(cart);
    }
    @GetMapping("/user/{userId}")
    @Operation(summary = "유저 카트 정보 검색", description = "유저가 가진 모든 카트 목록 검색")
    public ResponseEntity<List<CartResponseDto>> getUserCart(@PathVariable("userId") Long userId) {
        List<CartResponseDto> carts = cartService.findUserCarts(userId);
        return ResponseEntity.ok(carts);
    }

    @PostMapping
    @Operation(summary = "새 카트 생성", description = "새 카트 만들기")
    public ResponseEntity<CartResponseDto> saveCart(@RequestBody CartDto cartDto) {
        CartResponseDto cart = cartService.save(cartDto);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/{cartId}")
    @Operation(summary = "카트 업데이트", description = "카트 ID로 불러온 카트 정보 업데이트")
    public ResponseEntity<CartResponseDto> putCart(@PathVariable("cartId") Long cartId, @RequestBody @Validated CartDto cartDto) {
        CartResponseDto cart = cartService.updateCart(cartId, cartDto);
        return ResponseEntity.ok(cart);
    }

    @PatchMapping("/{cartId}")
    @Operation(summary = "카트 부분적 업데이트", description = "카트 ID로 불러온 카트 정보 부분적으로 업데이트")
    public ResponseEntity<CartResponseDto> patchCart(@PathVariable("cartId") Long cartId, @RequestBody @Validated CartDto cartDto) {
        CartResponseDto cart = cartService.updateCart(cartId, cartDto);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}")
    @Operation(summary = "카트 삭제", description = "카트 ID로 검색된 카트 삭제")
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

    @Trace
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
