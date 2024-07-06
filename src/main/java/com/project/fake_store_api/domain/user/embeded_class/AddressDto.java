package com.project.fake_store_api.domain.user.embeded_class;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private String city;
    private String street;
    private String zipcode;
    private int number;
    private GeolocationDto geolocationDto;
}
