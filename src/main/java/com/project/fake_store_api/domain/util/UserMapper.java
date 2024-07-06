package com.project.fake_store_api.domain.util;

import com.project.fake_store_api.domain.user.User;
import com.project.fake_store_api.domain.user.UserDto;
import com.project.fake_store_api.domain.user.embeded_class.*;

public class UserMapper {

    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setNameDto(NameDto.builder()
                .firstName(user.getName().getFirstName())
                .lastName(user.getName().getLastName())
                .build());
        userDto.setAddressDto(AddressDto.builder()
                .city(user.getAddress().getCity())
                .street(user.getAddress().getStreet())
                .zipcode(user.getAddress().getZipcode())
                .number(user.getAddress().getNumber())
                .geolocationDto(GeolocationDto.builder()
                        .lat(user.getAddress().getGeolocation().getLat())
                        .lng(user.getAddress().getGeolocation().getLng())
                        .build())
                .build());
        userDto.setPhone(user.getPhone());

        return userDto;
    }
}
