package com.project.fake_store_api.domain.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.Date;

public class BaseTimeEntityListener {

    @PrePersist
    public void prePersist(BaseTimeEntity entity) {
        Date now = new Date();
        entity.setCreatedAt(now);
        entity.setModifiedAt(now);
    }

    @PreUpdate
    public void preUpdate(BaseTimeEntity entity) {
        entity.setModifiedAt(new Date());
    }
}
