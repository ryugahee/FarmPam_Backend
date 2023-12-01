package com.fp.backend.system.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDProvider {
    private final UUID uuid4 = UUID.randomUUID();
    public String createUUID(){

        return String.valueOf(uuid4);
    }

}
