package ru.abogatyrev.websocket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Hamster on 26.01.2016.
 */
//@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class") - не использую, ибо меняет протокол...
/*
@JsonSubTypes({
        @JsonSubTypes.Type(Customer.class)
})
*/
@MappedSuperclass
public abstract class CommonItem implements Serializable, WebsocketData{

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private long id;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
