package ru.abogatyrev.websocket.server;

import ru.abogatyrev.websocket.WebSocketServerLogger;
import ru.abogatyrev.websocket.exceptions.CustomerNotFoundException;
import ru.abogatyrev.websocket.model.ApiToken;
import ru.abogatyrev.websocket.model.Customer;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Hamster on 28.01.2016.
 */
@Stateless
public class LoginBean implements Login {
    //TODO: перенести в конфиг
    // время действия токена 24 часа
    private static final int TOKEN_LIFETIME_HOURS = 24;

    @PersistenceContext
    private EntityManager entityManager;


    public Customer findCustomer(String email, String password) {
        String hql = "from Customer cst where cst.email=:email and cst.password=:password";
        Query query = entityManager.createQuery(hql)
                .setParameter("email", email)
                .setParameter("password", password);
        try {
            return (Customer) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public ApiToken findValidToken(Customer customer) {
        String hql = "from ApiToken tkn where tkn.customer=:customer and tkn.expiration > :dateTime";
        Query query = entityManager.createQuery(hql, ApiToken.class)
                .setParameter("customer", customer)
                .setParameter("dateTime", new Date(), TemporalType.TIMESTAMP);
        try {
            return (ApiToken) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public ApiToken refreshToken(ApiToken apiToken){
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.HOUR, TOKEN_LIFETIME_HOURS);

        apiToken.setExpiration(expiration.getTime());
        apiToken = entityManager.merge(apiToken);
        WebSocketServerLogger.LOG.refreshValidTokenDebug(apiToken.getToken(), apiToken.getExpiration().toString());

        return apiToken;
    }

    public ApiToken createToken(Customer customer){
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.HOUR, TOKEN_LIFETIME_HOURS);

        ApiToken apiToken = new ApiToken();
        apiToken.setCustomer(customer);
        apiToken.setToken(UUID.randomUUID().toString());
        apiToken.setExpiration(expiration.getTime());
        entityManager.persist(apiToken);
        WebSocketServerLogger.LOG.newTokenCreatedDebug(apiToken.getToken(), apiToken.getExpiration().toString());

        return apiToken;
    }

    /*
    public ApiToken refreshToken(String email, String password, String sequenceId) {
        Customer customer = findCustomer(email, password);

        if (customer == null) {
            throw WebSocketServerLogger.LOG.customerNotFoundException(email, sequenceId);
        }

        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.HOUR, TOKEN_LIFETIME_HOURS);

        ApiToken apiToken = findValidToken(customer);
        if (apiToken == null) {
            apiToken = new ApiToken();
            apiToken.setCustomer(customer);
            apiToken.setToken(UUID.randomUUID().toString());
            apiToken.setExpiration(expiration.getTime());
            entityManager.persist(apiToken);
            WebSocketServerLogger.LOG.newTokenCreatedDebug(apiToken.getToken(), apiToken.getExpiration().toString());
        } else {
            apiToken.setExpiration(expiration.getTime());
            apiToken = entityManager.merge(apiToken);
            WebSocketServerLogger.LOG.refreshValidTokenDebug(apiToken.getToken(), apiToken.getExpiration().toString());
        }

        return apiToken;
    }
    */
}
