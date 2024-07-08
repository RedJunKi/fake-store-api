package com.project.fake_store_api.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserLoginDto {

    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;

}
