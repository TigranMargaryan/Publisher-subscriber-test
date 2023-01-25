package com.app.publishsubscribe;

import com.app.publishsubscribe.domain.*;

public class StaticTestData {

    public static final String API_KEY = "apiKey";
    public static final String API_KEY_VALUE = "123456test";
    public static final String URL_ADD_MESSAGE = "/api/messages";
    public static final String URL_ADD_SUBSCRIBER = "/api/subscribers";
    public static final String URL_ADD_BROADCAST = "/api/broadcasts";

    public static String getSubscriberUrl(String id) {
        return "/api/subscribers/"+ id +"/messages";
    }

    public static String getRemoveSubscriberUrl(String id) {
        return "/api/subscribers/"+ id;
    }

    public static Subscriber addSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setName("TestName");
        return subscriber;
    }

    public static Payload addPayload1() {
        Payload payload = new Payload();
        payload.setType("age");
        payload.setData("18");
        return payload;
    }

    public static Payload addPayload2() {
        Payload payload = new Payload();
        payload.setType("name");
        payload.setData("Mike");
        return payload;
    }

    public static Payload addPayload3() {
        Payload payload = new Payload();
        payload.setType("Gender");
        payload.setData("Male");
        return payload;
    }
}
