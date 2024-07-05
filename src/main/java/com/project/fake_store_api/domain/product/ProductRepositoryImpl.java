package com.project.fake_store_api.domain.product;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ProductRepositoryImpl extends QuerydslRepositorySupport {

    public ProductRepositoryImpl() {
        super(Product.class);
    }

    public List<Product> findWithLimit(Long limit) {
        QProduct product = QProduct.product;
        JPQLQuery<Product> query = from(product);

        query.limit(limit);

        return query.fetch();
    }
}
