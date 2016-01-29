package ru.abogatyrev.websocket.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.abogatyrev.websocket.WebSocketServerLogger;
import ru.abogatyrev.websocket.model.Customer;
import ru.abogatyrev.websocket.server.WebsocketMessageWrapper;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Hamster on 26.01.2016.
 */
@ClientEndpoint
public class LoginEndpointClient {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static CountDownLatch latch;

    public static WebsocketMessageWrapper<Customer> request;
    public static String response;

    public static Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        try {
            session.getBasicRemote().sendText(messageToJson(request));
        } catch (IOException e) {
            WebSocketServerLogger.LOG.websocketClientError(e);
        }
    }

    @OnMessage
    public void processMessage(String message) {
        response = message;
        latch.countDown();
    }

    private static String messageToJson(WebsocketMessageWrapper message) throws JsonProcessingException {
        return objectMapper.writeValueAsString(message);
    }
}
