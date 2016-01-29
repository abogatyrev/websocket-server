package ru.abogatyrev.websocket.server;

import ru.abogatyrev.websocket.WebSocketServerLogger;
import ru.abogatyrev.websocket.exceptions.CustomerNotFoundException;
import ru.abogatyrev.websocket.model.ApiToken;
import ru.abogatyrev.websocket.model.Customer;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.UUID;

/**
 * Created by Hamster on 26.01.2016.
 */

@ServerEndpoint(
        value = "/login",
        encoders = {WebsocketMessageEncoder.class},
        decoders = {WebsocketCustomerMessageDecoder.class}
)
public class LoginEndpoint {

    @Inject
    private Login loginBean;

    @OnMessage
    public WebsocketMessageWrapper doLogin(WebsocketMessageWrapper<Customer> message) {
        Customer customer = loginBean.findCustomer(message.getData().getEmail(), message.getData().getPassword());
        if (customer == null) {
            throw WebSocketServerLogger.LOG.customerNotFoundException(message.getData().getEmail(), message.getSequenceId());
        }

        ApiToken apiToken = loginBean.findValidToken(customer);
        if(apiToken == null){
            apiToken = loginBean.createToken(customer);
        }else{
            apiToken = loginBean.refreshToken(apiToken);
        }

        //loginBean.refreshToken(customer.getEmail(), customer.getPassword(), message.getSequenceId());

        WebsocketMessageWrapper<ApiToken> token = new WebsocketMessageWrapper<>();
        token.setType(WebsocketMessageType.CUSTOMER_API_TOKEN);
        token.setSequenceId(message.getSequenceId());
        token.setData(apiToken);

        return token;
    }

    @OnOpen
    public void openLoginEndPoint(Session session) {
        WebSocketServerLogger.LOG.openWebsocketServerSessionDebug(session.getId());
    }

    @OnClose
    public void closeLoginEndPoint(Session session) {
        WebSocketServerLogger.LOG.closeWebsocketServerSessionDebug(session.getId());
    }

    @OnError
    public void errorHandle(Session session, Throwable error) {
        if (session != null && session.isOpen()) {
            WebsocketMessageWrapper<Error> response = new WebsocketMessageWrapper<>();
            response.setType(WebsocketMessageType.CUSTOMER_ERROR);
            response.setData(new Error());
            if(error instanceof CustomerNotFoundException){
                response.setSequenceId(((CustomerNotFoundException) error).getSequenceId());
                response.getData().setCode("customer.notFound");
                response.getData().setDescription("Customer not found");
            }else {
                WebSocketServerLogger.LOG.handleWebsocketServerError(error);

                response.setSequenceId(UUID.randomUUID().toString());
                response.getData().setCode(error.getClass().getName());
                response.getData().setDescription("Handled exception");
            }

            session.getAsyncRemote().sendObject(response);
        }
    }
}
