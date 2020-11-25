package ru.gaidamaka.protocol.message;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    @NotNull
    private final String name;


    public User(@NotNull String name) {
        this.name = Objects.requireNonNull(name, "User name cant be null");
    }

    public @NotNull String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
