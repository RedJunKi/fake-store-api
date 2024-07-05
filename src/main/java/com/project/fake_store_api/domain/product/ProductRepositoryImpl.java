package com.project.fake_store_api.domain.product;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
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

    public List<Category> findCategories() {
        QProduct product = QProduct.product;
        JPQLQuery<Category> query = from(product)
                .select(product.category)
                .distinct();

        return query.fetch();
    }

    public List<Product> findProductWithCategory(String detailCategory) {
        QProduct product = QProduct.product;
        JPQLQuery<Product> query = from(product)
                .select(product)
                .where(product.category.eq(Category.valueOf(detailCategory)));

        return query.fetch();
    }
}
