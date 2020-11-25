package ru.gaidamaka.server;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.protocol.message.User;
import ru.gaidamaka.server.exception.InvalidUserNameException;

import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RequestHandlersRepository implements ClientsRepository {
    private final Map<User, ClientHandler> usersList = new ConcurrentHashMap<>();
    private final List<ClientHandler> unregisteredHandlers = new CopyOnWriteArrayList<>();

    private boolean isUserLogged(@NotNull User user) {
        return usersList.containsKey(user);
    }

    @Override
    public void registerUser(@NotNull User user, @NotNull ClientHandler clientHandler) {
        Objects.requireNonNull(clientHandler, "Handler cant be null");
        Objects.requireNonNull(user, "User cant be null");
        validateRegistration(user, clientHandler);
        unregisteredHandlers.remove(clientHandler);
        usersList.put(user, clientHandler);
    }

    private void validateRegistration(@NotNull User user, @NotNull ClientHandler clientHandler) {
        if (usersList.containsValue(clientHandler)) {
            throw new IllegalArgumentException("Cant login when user already logged");
        }
        if (!unregisteredHandlers.contains(clientHandler)) {
            throw new IllegalArgumentException("Cant register unknown handler");
        }
        if (isUserLogged(user)) {
            throw new InvalidUserNameException("User with name=" + user.getName() + " already registered");
        }
    }

    @Override
    public void removeUser(@NotNull User user) {
        Objects.requireNonNull(user, "User cant be null");
        usersList.remove(user);
    }

    @Override
    public void removeUnregisteredConnection(@NotNull ClientHandler clientHandler) {
        Objects.requireNonNull(clientHandler, "Client handler cant be null");
        unregisteredHandlers.remove(clientHandler);
    }

    @Override
    public @NotNull Collection<User> getAllUsers() {
        return usersList.keySet();
    }

    @Override
    @NotNull
    public Collection<ClientHandler> getAllClientHandlers() {
        return usersList.values();
    }

    public void startNewClientHandle(@NotNull Socket socket) {
        Objects.requireNonNull(socket, "Socket cant be null");
        RequestHandler requestHandler = new RequestHandler(socket, this);
        unregisteredHandlers.add(requestHandler);
        new Thread(requestHandler).start();
    }
}
