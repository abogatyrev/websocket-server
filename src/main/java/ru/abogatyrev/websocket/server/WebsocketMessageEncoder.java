package ru.abogatyrev.websocket.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by Hamster on 27.01.2016.
 */
public class WebsocketMessageEncoder implements Encoder.Text<WebsocketMessageWrapper> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String encode(WebsocketMessageWrapper websocketMessageWrapper) throws EncodeException {
        try {
            return objectMapper.writeValueAsString(websocketMessageWrapper);
        }catch (JsonProcessingException e){
            throw new EncodeException(websocketMessageWrapper, e.getMessage(), e);
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
