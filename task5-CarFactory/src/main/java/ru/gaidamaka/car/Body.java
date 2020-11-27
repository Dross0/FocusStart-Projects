package ru.gaidamaka.car;


import com.google.common.base.MoreObjects;
import ru.gaidamaka.AbstractUniqueObject;


public class Body extends AbstractUniqueObject {
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("uuid", getUUID())
                .toString();
    }
}
