package com.project.fake_store_api.domain.user.embeded_class;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Geolocation {
    private String lat;
    private String lng;
}
