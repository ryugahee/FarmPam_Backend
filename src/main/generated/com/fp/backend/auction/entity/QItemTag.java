package com.fp.backend.auction.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemTag is a Querydsl query type for ItemTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemTag extends EntityPathBase<ItemTag> {

    private static final long serialVersionUID = 835675256L;

    public static final QItemTag itemTag = new QItemTag("itemTag");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<ItemTagMap, QItemTagMap> itemTagMapList = this.<ItemTagMap, QItemTagMap>createList("itemTagMapList", ItemTagMap.class, QItemTagMap.class, PathInits.DIRECT2);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regTime = _super.regTime;

    public final StringPath tagName = createString("tagName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public QItemTag(String variable) {
        super(ItemTag.class, forVariable(variable));
    }

    public QItemTag(Path<? extends ItemTag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItemTag(PathMetadata metadata) {
        super(ItemTag.class, metadata);
    }

}

