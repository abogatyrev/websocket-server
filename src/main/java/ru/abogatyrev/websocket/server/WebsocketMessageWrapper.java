package ru.abogatyrev.websocket.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.abogatyrev.websocket.model.WebsocketData;

import javax.websocket.EncodeException;

/**
 * Created by Hamster on 27.01.2016.
 */
@JsonPropertyOrder({"type", "sequence_id", "data"})
public class WebsocketMessageWrapper<T extends WebsocketData> {
    private WebsocketMessageType type;

    @JsonProperty(value = "sequence_id")
    private String sequenceId;

    private T data;

    public WebsocketMessageType getType() {
        return type;
    }

    public void setType(WebsocketMessageType type) {
        this.type = type;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
