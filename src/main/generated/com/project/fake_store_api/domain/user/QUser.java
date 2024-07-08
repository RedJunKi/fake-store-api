package com.project.fake_store_api.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -2008405410L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.project.fake_store_api.domain.common.QBaseTimeEntity _super = new com.project.fake_store_api.domain.common.QBaseTimeEntity(this);

    public final com.project.fake_store_api.domain.user.embeded_class.QAddress address;

    public final ListPath<com.project.fake_store_api.domain.cart.Cart, com.project.fake_store_api.domain.cart.QCart> carts = this.<com.project.fake_store_api.domain.cart.Cart, com.project.fake_store_api.domain.cart.QCart>createList("carts", com.project.fake_store_api.domain.cart.Cart.class, com.project.fake_store_api.domain.cart.QCart.class, PathInits.DIRECT2);

    //inherited
    public final DatePath<java.util.Date> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.fake_store_api.domain.user.embeded_class.QName name;

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final SetPath<com.project.fake_store_api.domain.role.Role, com.project.fake_store_api.domain.role.QRole> roles = this.<com.project.fake_store_api.domain.role.Role, com.project.fake_store_api.domain.role.QRole>createSet("roles", com.project.fake_store_api.domain.role.Role.class, com.project.fake_store_api.domain.role.QRole.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new com.project.fake_store_api.domain.user.embeded_class.QAddress(forProperty("address"), inits.get("address")) : null;
        this.name = inits.isInitialized("name") ? new com.project.fake_store_api.domain.user.embeded_class.QName(forProperty("name")) : null;
    }

}

