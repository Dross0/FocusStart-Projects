package ru.gaidamaka.protocol;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class SerializableObjectsConnection implements Closeable {
    @NotNull
    private final Socket socket;
    private final ObjectOutputStream writer;
    private final ObjectInputStream reader;

    public SerializableObjectsConnection(@NotNull Socket socket) throws IOException {
        this.socket = Objects.requireNonNull(socket, "Socket cant be null");
        this.writer = new ObjectOutputStream(socket.getOutputStream());
        this.reader = new ObjectInputStream(socket.getInputStream());
    }

    public void sendObject(@NotNull Serializable obj) throws IOException {
        Objects.requireNonNull(obj);
        writer.writeObject(obj);
        writer.flush();
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return reader.readObject();
    }

    @Override
    public void close() throws IOException {
        socket.close();
        writer.close();
        reader.close();
    }
}
