package ru.abogatyrev.websocket.exceptions;

import javax.ejb.EJBException;

/**
 * Created by Hamster on 28.01.2016.
 */
public class CustomerNotFoundException extends EJBException {
    private String sequenceId;

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public CustomerNotFoundException() {
        super();
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(Exception ex) {
        super(ex);
    }

    public CustomerNotFoundException(String message, Exception ex) {
        super(message, ex);
    }
}
