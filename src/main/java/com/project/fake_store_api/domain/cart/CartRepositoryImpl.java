package com.project.fake_store_api.domain.cart;

import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class CartRepositoryImpl extends QuerydslRepositorySupport implements CartRepositoryCustom {

    public CartRepositoryImpl() {
        super(Cart.class);
    }

    @Override
    public List<Cart> findWithLimit(Long limit) {
        QCart cart = QCart.cart;
        JPQLQuery<Cart> query = from(cart).limit(limit);

        return query.fetch();
    }
}
