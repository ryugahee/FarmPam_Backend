package com.fp.backend.auction.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemTagMap is a Querydsl query type for ItemTagMap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemTagMap extends EntityPathBase<ItemTagMap> {

    private static final long serialVersionUID = 1971180996L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemTagMap itemTagMap = new QItemTagMap("itemTagMap");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final QItemTag itemTag;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regTime = _super.regTime;

    public QItemTagMap(String variable) {
        this(ItemTagMap.class, forVariable(variable), INITS);
    }

    public QItemTagMap(Path<? extends ItemTagMap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemTagMap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemTagMap(PathMetadata metadata, PathInits inits) {
        this(ItemTagMap.class, metadata, inits);
    }

    public QItemTagMap(Class<? extends ItemTagMap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item")) : null;
        this.itemTag = inits.isInitialized("itemTag") ? new QItemTag(forProperty("itemTag")) : null;
    }

}

