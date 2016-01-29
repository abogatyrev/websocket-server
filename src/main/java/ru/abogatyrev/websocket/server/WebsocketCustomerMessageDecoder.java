package ru.abogatyrev.websocket.server;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.abogatyrev.websocket.model.Customer;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import java.io.IOException;

/**
 * Created by Hamster on 27.01.2016.
 */
public class WebsocketCustomerMessageDecoder implements Decoder.Text<WebsocketMessageWrapper> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public WebsocketMessageWrapper decode(String s) throws DecodeException {
        try {
            WebsocketMessageWrapper<Customer> customerLoginRequest = objectMapper.readValue(s,
                    new TypeReference<WebsocketMessageWrapper<Customer>>(){});
            return customerLoginRequest;
        }catch (IOException e){
            throw new DecodeException(s, e.getMessage(), e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
