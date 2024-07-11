package com.project.fake_store_api.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.fake_store_api.domain.role.Role;
import com.project.fake_store_api.domain.role.RoleStatus;
import com.project.fake_store_api.security.token.TokenResponseDto;
import com.project.fake_store_api.security.util.JwtTokenizer;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenizer jwtTokenizer;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getUsers() throws Exception {
        List<UserDto> users = new ArrayList<>();

        when(userService.findAll()).thenReturn(users);
        when(userService.findWithLimit(10L)).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getUser() throws Exception {

        when(userService.findOne(1L)).thenReturn(new UserDto());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void saveUser() throws Exception {
        UserDto userDto = new UserDto();
        when(userService.save(userDto)).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void login() throws Exception {

        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("user@example.com");
        userLoginDto.setPassword("password");

        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L, RoleStatus.ROLE_USER));

        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setRoles(roles);

        when(userService.login(userLoginDto)).thenReturn(user);

        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        when(jwtTokenizer.createAccessToken(anyLong(), anyString(), anyList())).thenReturn(accessToken);
        when(jwtTokenizer.createRefreshToken(anyLong(), anyString(), anyList())).thenReturn(refreshToken);

        mockMvc.perform(post("/users/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void updateUser() throws Exception {
        UserDto userDto = new UserDto(1L);
        when(userService.update(1L, userDto)).thenReturn(userDto);

        mockMvc.perform(put("/users/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteUser() throws Exception {
        UserDto userDto = new UserDto(1L);
        when(userService.delete(1L)).thenReturn(userDto);

        mockMvc.perform(delete("/users/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}