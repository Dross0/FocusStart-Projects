package ru.gaidamaka.protocol.message;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class User {
    @NotNull
    private final String name;

    @NotNull
    private final UUID id;

    public User(@NotNull String name) {
        this.name = Objects.requireNonNull(name, "User name cant be null");
        this.id = UUID.randomUUID();
    }

    @NotNull
    public UUID getId() {
        return id;
    }

    public @NotNull String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}
