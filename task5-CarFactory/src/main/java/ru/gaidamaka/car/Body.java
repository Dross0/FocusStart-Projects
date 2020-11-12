package ru.gaidamaka.car;


import com.google.common.base.MoreObjects;

public class Body implements Detail {
    private static int bodyNumbers = 0;

    private final int id;

    public Body() {
        bodyNumbers++;
        id = bodyNumbers;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
