package ru.abogatyrev.websocket.server;

import ru.abogatyrev.websocket.model.ApiToken;
import ru.abogatyrev.websocket.model.Customer;

/**
 * Created by Hamster on 29.01.2016.
 */
public interface Login {
    Customer findCustomer(String email, String password);
    ApiToken findValidToken(Customer customer);

    ApiToken refreshToken(ApiToken apiToken);
    ApiToken createToken(Customer customer);

    //ApiToken refreshToken(String email, String password, String sequenceId);
}
