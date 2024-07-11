package com.project.fake_store_api.domain.user;

import com.project.fake_store_api.domain.role.Role;
import com.project.fake_store_api.global.annotation.Trace;
import com.project.fake_store_api.security.token.TokenResponseDto;
import com.project.fake_store_api.security.util.JwtTokenizer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 API")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;

    @Trace
    @GetMapping
    @Operation(summary = "모든 유저 정보 검색", description = "필터링 및 정렬(선택사항)을 사용하여 모든 유저 목록 가져오기")
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(value = "limit", required = false) Long limit,
                                                  @RequestParam(value = "sort", required = false) String condition) {

        List<UserDto> result = fetchUsers(limit);
        result = sortUsers(result, condition);

        return ResponseEntity.ok(result);
    }

    @GetMapping("{userId}")
    @Operation(summary = "유저 정보 검색", description = "유저 ID로 유저정보 가져오기")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") Long userId) {
        UserDto result = userService.findOne(userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "유저 등록", description = "새로운 유저 등록")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        UserDto result = userService.save(userDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "ID, PASSWORD 확인 후 로그인 처리(토큰 발급)")
    public ResponseEntity<TokenResponseDto> login(@Validated @RequestBody UserLoginDto userLoginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User user = userService.login(userLoginDto);

        List<String> roles = user.getRoles().stream()
                .map(Role::getRoleStatus)
                .map(String::valueOf)
                .toList();

        String accessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), roles);
        String refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), roles);

        TokenResponseDto result = new TokenResponseDto();
        result.setAccessToken(accessToken);
        result.setRefreshToken(refreshToken);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "유저 정보 업데이트", description = "유저 ID로 검색된 유저 정보 업데이트")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") Long userId, @RequestBody UserDto userDto) {
        UserDto result = userService.update(userId, userDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "유저 삭제", description = "유저 ID로 검색된 유저 삭제")
    public ResponseEntity<UserDto> deleteUser(@PathVariable("userId") Long userId) {
        UserDto result = userService.delete(userId);
        return ResponseEntity.ok(result);
    }

    private List<UserDto> sortUsers(List<UserDto> users, String condition) {
        if (condition == null) {
            return users;
        }

        if (condition.equals("asc")) {
            return users.stream()
                    .sorted(Comparator.comparing(UserDto::getId))
                    .toList();

        } else if (condition.equals("desc")) {
            return users.stream()
                    .sorted(Comparator.comparing(UserDto::getId).reversed())
                    .toList();
        } else {
            return users;
        }
    }

    private List<UserDto> fetchUsers(Long limit) {
        if (limit != null && limit > 0) {
            return userService.findWithLimit(limit);
        } else {
            return userService.findAll();
        }
    }
}
