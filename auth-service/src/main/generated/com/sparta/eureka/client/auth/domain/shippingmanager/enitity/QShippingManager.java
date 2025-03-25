package com.sparta.eureka.client.auth.domain.shippingmanager.enitity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QShippingManager is a Querydsl query type for ShippingManager
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShippingManager extends EntityPathBase<ShippingManager> {

    private static final long serialVersionUID = -826668124L;

    public static final QShippingManager shippingManager = new QShippingManager("shippingManager");

    public final com.sparta.eureka.client.auth.common.jpa.QBaseEntity _super = new com.sparta.eureka.client.auth.common.jpa.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final NumberPath<Long> deletedBy = _super.deletedBy;

    public final NumberPath<Integer> deliveryOrder = createNumber("deliveryOrder", Integer.class);

    public final ComparablePath<java.util.UUID> hubId = createComparable("hubId", java.util.UUID.class);

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath slackId = createString("slackId");

    public final EnumPath<ShippingManagerType> type = createEnum("type", ShippingManagerType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Long> updatedBy = _super.updatedBy;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QShippingManager(String variable) {
        super(ShippingManager.class, forVariable(variable));
    }

    public QShippingManager(Path<? extends ShippingManager> path) {
        super(path.getType(), path.getMetadata());
    }

    public QShippingManager(PathMetadata metadata) {
        super(ShippingManager.class, metadata);
    }

}

