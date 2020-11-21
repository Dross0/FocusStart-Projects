package ru.gaidamaka.server;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.protocol.message.User;

import java.util.Collection;


public interface ClientsRepository {
    void registerUser(@NotNull User user, @NotNull ClientHandler clientHandler);

    void removeUser(@NotNull User user);

    @NotNull
    Collection<User> getAllUsers();

    @NotNull
    Collection<ClientHandler> getAllClientHandlers();
}
