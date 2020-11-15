package ru.gaidamaka.car;


import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Body implements Detail {
    private final @NotNull UUID uuid;

    public Body() {
        uuid = UUID.randomUUID();
    }

    @Override
    public @NotNull UUID getID() {
        return uuid;
    }

    @Override
    public @NotNull String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", uuid)
                .toString();
    }
}
