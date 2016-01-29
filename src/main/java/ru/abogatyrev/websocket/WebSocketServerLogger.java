package ru.abogatyrev.websocket;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.annotations.*;
import ru.abogatyrev.websocket.exceptions.CustomerNotFoundException;

import static org.jboss.logging.annotations.Message.Format.MESSAGE_FORMAT;

/**
 * Created by Hamster on 27.01.2016.
 */
@MessageLogger(projectCode = "WEBSOCKET_DEMO_", length = 4)
@ValidIdRanges
        ({
                // log messages
                @ValidIdRange(min = 1000, max = 4999),
                // exceptions
                @ValidIdRange(min = 5000, max = 9999)
        })
public interface WebSocketServerLogger extends BasicLogger {
    WebSocketServerLogger LOG = Logger.getMessageLogger(WebSocketServerLogger.class, WebSocketServerLogger.class.getPackage().getName());

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 1000, format = Message.Format.MESSAGE_FORMAT, value = "Server WebSocket opened: {0}")
    void openWebsocketServerSessionDebug(String sessionId);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 1001, format = Message.Format.MESSAGE_FORMAT, value = "Server closing WebSocket: {0}")
    void closeWebsocketServerSessionDebug(String sessionId);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 1002, value = "Server handle WebSocket error")
    void handleWebsocketServerError(@Cause Throwable error);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 1003, value = "Client open WebSocket error")
    void websocketClientError(@Cause Throwable error);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 1004, format = Message.Format.MESSAGE_FORMAT, value = "Valid token refreshed: {0}, {1}")
    void refreshValidTokenDebug(String token, String expiration);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 1005, format = Message.Format.MESSAGE_FORMAT, value = "New token created: {0}, {1}")
    void newTokenCreatedDebug(String token, String expiration);

    @Message(id = 5000, format = MESSAGE_FORMAT,
            value = "Customer ''{0}'' not found or password is incorrect")
    CustomerNotFoundException customerNotFoundException(String email, @Property(name = "sequenceId") String sequenceId);
}
