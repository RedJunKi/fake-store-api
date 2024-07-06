package com.project.fake_store_api.domain.user.embeded_class;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

@Embeddable
@Data
public class Address {

    private String city;
    private String street;
    private String zipcode;
    private int number;
    @Embedded
    private Geolocation geolocation;

}
