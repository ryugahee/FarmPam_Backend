package com.fp.backend.auction.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = 1662596034L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isSoldout = createBoolean("isSoldout");

    public final StringPath itemDetail = createString("itemDetail");

    public final ListPath<ItemTagMap, QItemTagMap> itemTagMapList = this.<ItemTagMap, QItemTagMap>createList("itemTagMapList", ItemTagMap.class, QItemTagMap.class, PathInits.DIRECT2);

    public final StringPath itemTitle = createString("itemTitle");

    public final StringPath itemType = createString("itemType");

    public final NumberPath<Integer> minPrice = createNumber("minPrice", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regTime = _super.regTime;

    public final NumberPath<Long> time = createNumber("time", Long.class);

    public final com.fp.backend.account.entity.QUsers users;

    public final NumberPath<Integer> weight = createNumber("weight", Integer.class);

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.users = inits.isInitialized("users") ? new com.fp.backend.account.entity.QUsers(forProperty("users")) : null;
    }

}

