package ru.gaidamaka.server;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.protocol.message.User;

import java.util.Collection;


public interface ClientsRepository {
    void registerUser(@NotNull User user, @NotNull ClientHandler clientHandler);

    void removeUser(@NotNull User user);

    void removeUnregisteredConnection(@NotNull ClientHandler clientHandler);

    @NotNull
    Collection<User> getAllUsers();

    @NotNull
    Collection<ClientHandler> getAllClientHandlers();
}
