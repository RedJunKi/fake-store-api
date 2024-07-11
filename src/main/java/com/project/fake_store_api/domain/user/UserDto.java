package com.project.fake_store_api.domain.user;

import com.project.fake_store_api.domain.user.embeded_class.Address;
import com.project.fake_store_api.domain.user.embeded_class.AddressDto;
import com.project.fake_store_api.domain.user.embeded_class.Name;
import com.project.fake_store_api.domain.user.embeded_class.NameDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String username;
    private String password;
    private NameDto nameDto;
    private AddressDto addressDto;
    private String phone;

    public UserDto(Long id) {
        this.id = id;
    }
}
