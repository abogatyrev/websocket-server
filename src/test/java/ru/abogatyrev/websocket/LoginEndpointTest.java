package ru.abogatyrev.websocket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.abogatyrev.websocket.client.LoginEndpointClient;
import ru.abogatyrev.websocket.exceptions.CustomerNotFoundException;
import ru.abogatyrev.websocket.model.ApiToken;
import ru.abogatyrev.websocket.model.CommonItem;
import ru.abogatyrev.websocket.model.Customer;
import ru.abogatyrev.websocket.model.WebsocketData;
import ru.abogatyrev.websocket.server.*;
import ru.abogatyrev.websocket.server.Error;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by Hamster on 26.01.2016.
 */
@RunWith(Arquillian.class)
public class LoginEndpointTest {

    private static final WebsocketMessageWrapper<Customer> successCustomerLoginMessage;

    static {
        successCustomerLoginMessage = new WebsocketMessageWrapper<>();
        successCustomerLoginMessage.setData(new Customer());

        successCustomerLoginMessage.setType(WebsocketMessageType.LOGIN_CUSTOMER);
        successCustomerLoginMessage.setSequenceId(UUID.randomUUID().toString());

        successCustomerLoginMessage.getData().setEmail("fpi@bk.ru");
        successCustomerLoginMessage.getData().setPassword("123123");
    }

    private static final WebsocketMessageWrapper<Customer> unknownCustomerLoginMessage;

    static {
        unknownCustomerLoginMessage = new WebsocketMessageWrapper<>();
        unknownCustomerLoginMessage.setData(new Customer());

        unknownCustomerLoginMessage.setType(WebsocketMessageType.LOGIN_CUSTOMER);
        unknownCustomerLoginMessage.setSequenceId(UUID.randomUUID().toString());

        unknownCustomerLoginMessage.getData().setEmail("123@gmail.com");
        unknownCustomerLoginMessage.getData().setPassword("newPassword");
    }

    @ArquillianResource
    private URI base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive result = ShrinkWrap.create(WebArchive.class, "websocket-server.war")
                .addClasses(
                        WebsocketData.class,
                        CommonItem.class,
                        Customer.class,
                        ApiToken.class,
                        Error.class,
                        CustomerNotFoundException.class,

                        Login.class,
                        LoginBean.class,
                        LoginEndpoint.class,
                        WebsocketCustomerMessageDecoder.class,
                        WebsocketMessageEncoder.class,
                        WebsocketMessageType.class,
                        WebsocketMessageWrapper.class,

                        LoginEndpointClient.class
                )
                .addPackage(WebSocketServerLogger.class.getPackage())
                .addAsWebInfResource("META-INF/persistence.xml", "classes/META-INF/persistence.xml")
                ;

        System.out.println(result.toString(true));

        return result;
    }

    @Test
    public void valid_customer_should_return_api_token() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        LoginEndpointClient.latch = new CountDownLatch(1);
        LoginEndpointClient.request = successCustomerLoginMessage;

        Session session = connectToServer(LoginEndpointClient.class);
        assertNotNull(session);
        assertTrue(LoginEndpointClient.latch.await(2, TimeUnit.SECONDS));

        ObjectMapper objectMapper = new ObjectMapper();
        WebsocketMessageWrapper<ApiToken> response = objectMapper.readValue(LoginEndpointClient.response, new TypeReference<WebsocketMessageWrapper<ApiToken>>() {
        });

        assertEquals(successCustomerLoginMessage.getSequenceId(), response.getSequenceId());
        assertEquals(WebsocketMessageType.CUSTOMER_API_TOKEN, response.getType());

        System.out.println(LoginEndpointClient.response);
    }

    @Test
    public void unknown_customer_should_return_customer_error() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        LoginEndpointClient.latch = new CountDownLatch(1);
        LoginEndpointClient.request = unknownCustomerLoginMessage;

        Session session = connectToServer(LoginEndpointClient.class);
        assertNotNull(session);
        assertTrue(LoginEndpointClient.latch.await(2, TimeUnit.SECONDS));

        ObjectMapper objectMapper = new ObjectMapper();
        WebsocketMessageWrapper<Error> response = objectMapper.readValue(LoginEndpointClient.response, new TypeReference<WebsocketMessageWrapper<Error>>() {
        });

        assertEquals(unknownCustomerLoginMessage.getSequenceId(), response.getSequenceId());
        assertEquals(WebsocketMessageType.CUSTOMER_ERROR, response.getType());

        System.out.println(LoginEndpointClient.response);
    }

    @Test
    public void wrong_request_should_return_customer_error_handled_error() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        LoginEndpointClient.latch = new CountDownLatch(1);
        LoginEndpointClient.request = new WebsocketMessageWrapper<>(); // wrong empty response

        Session session = connectToServer(LoginEndpointClient.class);
        assertNotNull(session);
        assertTrue(LoginEndpointClient.latch.await(2, TimeUnit.SECONDS));

        ObjectMapper objectMapper = new ObjectMapper();
        WebsocketMessageWrapper<Error> response = objectMapper.readValue(LoginEndpointClient.response, new TypeReference<WebsocketMessageWrapper<Error>>() {
        });

        assertEquals(WebsocketMessageType.CUSTOMER_ERROR, response.getType());

        System.out.println(LoginEndpointClient.response);
    }

    public Session connectToServer(Class<?> endpoint) throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = new URI("ws://" + base.getHost() + ":" + base.getPort() + base.getPath() + "login");
        return container.connectToServer(endpoint, uri);
    }

}
