package com.app.publishsubscribe;

import com.app.publishsubscribe.domain.*;
import org.junit.After;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Objects;

import static com.app.publishsubscribe.StaticTestData.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PublishSubscribeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PubSubControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    @Order(1)
    public void addSubscriberTest() {
        HttpEntity<Subscriber> entity = new HttpEntity<>(addSubscriber(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(URL_ADD_SUBSCRIBER),
                HttpMethod.POST, entity, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    @Order(2)
    public void addMessageToQueueTest() throws InterruptedException {
        headers.add(API_KEY, API_KEY_VALUE);
        HttpEntity<Payload> entity1 = new HttpEntity<>(addPayload1(), headers);
        ResponseEntity<String> response1 = restTemplate.exchange(
                createURLWithPort(URL_ADD_MESSAGE),
                HttpMethod.POST, entity1, String.class);
        Thread.sleep(5000);
        HttpEntity<Payload> entity2 = new HttpEntity<>(addPayload2(), headers);
        ResponseEntity<String> response2 = restTemplate.exchange(
                createURLWithPort(URL_ADD_MESSAGE),
                HttpMethod.POST, entity2, String.class);
        Thread.sleep(5000);
        HttpEntity<Payload> entity3 = new HttpEntity<>(addPayload3(), headers);
        ResponseEntity<String> response3 = restTemplate.exchange(
                createURLWithPort(URL_ADD_MESSAGE),
                HttpMethod.POST, entity3, String.class);
        Thread.sleep(5000);
        assertEquals(response1.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response2.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response3.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    @Order(3)
    public void broadcastMessagesTest() {
        headers.add(API_KEY, API_KEY_VALUE);
        HttpEntity<Subscriber> entityBroadcast = new HttpEntity<>(addSubscriber(), headers);

        ResponseEntity<String> responseBroadcast = restTemplate.exchange(
                createURLWithPort(URL_ADD_BROADCAST),
                HttpMethod.POST, entityBroadcast, String.class);

        assertEquals(responseBroadcast.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    @Order(4)
    public void getMessagesForSubscriberTest() {
        headers.add(API_KEY, API_KEY_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(getSubscriberUrl("1")),
                HttpMethod.GET, entity, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(response.getBody()).contains(addPayload1().getData()));
        assertTrue(Objects.requireNonNull(response.getBody()).contains(addPayload2().getData()));
        assertTrue(Objects.requireNonNull(response.getBody()).contains(addPayload3().getData()));
    }

    @Test
    @Order(5)
    public void removeSubscriber() {
        headers.add(API_KEY, API_KEY_VALUE);
        HttpEntity<Subscriber> entityBroadcast = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseBroadcast = restTemplate.exchange(
                createURLWithPort(getRemoveSubscriberUrl("1")),
                HttpMethod.DELETE, entityBroadcast, String.class);

        assertEquals(responseBroadcast.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @After
    public void afterTest() {
        jdbcTemplate.execute("DROP DATABASE " + "publish_subscribe_test");
    }
}