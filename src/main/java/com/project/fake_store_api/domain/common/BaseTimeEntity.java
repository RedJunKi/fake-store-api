package com.project.fake_store_api.domain.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(BaseTimeEntityListener.class)
public class BaseTimeEntity {

    @Temporal(TemporalType.DATE)
    private Date createdAt;

}
