package com.project.fake_store_api.domain.user.embeded_class;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGeolocation is a Querydsl query type for Geolocation
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QGeolocation extends BeanPath<Geolocation> {

    private static final long serialVersionUID = -893435242L;

    public static final QGeolocation geolocation = new QGeolocation("geolocation");

    public final StringPath lat = createString("lat");

    public final StringPath lng = createString("lng");

    public QGeolocation(String variable) {
        super(Geolocation.class, forVariable(variable));
    }

    public QGeolocation(Path<? extends Geolocation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGeolocation(PathMetadata metadata) {
        super(Geolocation.class, metadata);
    }

}

