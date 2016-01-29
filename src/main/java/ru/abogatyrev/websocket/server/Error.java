package ru.abogatyrev.websocket.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ru.abogatyrev.websocket.model.WebsocketData;

/**
 * Created by Hamster on 27.01.2016.
 */
public class Error implements WebsocketData {
    @JsonProperty(value = "error_description")
    private String description;

    @JsonProperty(value = "error_code")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
