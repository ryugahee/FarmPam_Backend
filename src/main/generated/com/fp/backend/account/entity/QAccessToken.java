package com.fp.backend.account.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccessToken is a Querydsl query type for AccessToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccessToken extends EntityPathBase<AccessToken> {

    private static final long serialVersionUID = 1767518012L;

    public static final QAccessToken accessToken1 = new QAccessToken("accessToken1");

    public final StringPath accessToken = createString("accessToken");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath username = createString("username");

    public QAccessToken(String variable) {
        super(AccessToken.class, forVariable(variable));
    }

    public QAccessToken(Path<? extends AccessToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccessToken(PathMetadata metadata) {
        super(AccessToken.class, metadata);
    }

}

