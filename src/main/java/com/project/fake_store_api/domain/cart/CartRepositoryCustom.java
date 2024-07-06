package com.project.fake_store_api.domain.cart;

import java.util.List;

public interface CartRepositoryCustom {
    List<Cart> findWithLimit(Long limit);
}
