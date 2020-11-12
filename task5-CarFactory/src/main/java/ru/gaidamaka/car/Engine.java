package ru.gaidamaka.car;


import com.google.common.base.MoreObjects;

public class Engine implements Detail {
    private static int engineNumbers = 0;

    private final int id;

    public Engine() {
        engineNumbers++;
        id = engineNumbers;
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
