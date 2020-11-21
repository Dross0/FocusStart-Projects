package ru.gaidamaka.protocol.message;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ResponseMessage extends Message {
    @NotNull
    private final RequestMessage requestMessage;

    @NotNull
    private final ResponseStatusCode responseStatusCode;

    public ResponseMessage(@NotNull String content,
                           @NotNull RequestMessage requestMessage,
                           @NotNull ResponseStatusCode responseStatusCode) {
        super(content, MessageType.SERVER_RESPONSE);
        this.requestMessage = Objects.requireNonNull(requestMessage, "Request message cant be null");
        this.responseStatusCode = responseStatusCode;
    }

    public ResponseMessage(@NotNull RequestMessage requestMessage, @NotNull ResponseStatusCode responseStatusCode) {
        this(responseStatusCode.name(), requestMessage, responseStatusCode);
    }

    @NotNull
    public RequestMessage getRequestMessage() {
        return requestMessage;
    }

    @NotNull
    public ResponseStatusCode getResponseStatusCode() {
        return responseStatusCode;
    }
}
