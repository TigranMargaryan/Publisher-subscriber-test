package com.app.publishsubscribe.controller;

import com.app.publishsubscribe.domain.*;
import com.app.publishsubscribe.service.PubSubService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PubSubController {

    private final PubSubService pubSubService;

    @Value("${apiKey}")
    private String API_KEY;

    @GetMapping("/subscribers/{id}/messages")
    public ResponseEntity<List<Message>> getMessagesForSubscriber(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable Long id,
            @RequestParam(value = "sort", defaultValue = "asc") String sort,
            @RequestParam(value = "createdDate", required = false) String createdDate,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        if(!API_KEY.equals(apiKey)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<Message> messages = pubSubService.getMessagesForSubscriber(id, sort, createdDate, pageNo, pageSize);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping("/subscribers")
    public ResponseEntity<Void> addSubscriber(@RequestBody Subscriber subscriber) {
        pubSubService.addSubscriber(subscriber);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/messages")
    public ResponseEntity<String> addMessageToQueue(@RequestHeader("apiKey") String apiKey, @RequestBody Payload payload) {
        pubSubService.addMessageToQueue(payload);
        return unauthorized(apiKey, HttpStatus.CREATED);
    }

    @PostMapping("/broadcasts")
    public ResponseEntity<String> broadcastMessages(@RequestHeader("apiKey") String apiKey, @RequestBody Subscriber subscriber) {
        pubSubService.broadcastMessages(subscriber);
        return unauthorized(apiKey, HttpStatus.CREATED);
    }

    @DeleteMapping("/subscribers/{id}")
    public ResponseEntity<String> removeSubscriber(@RequestHeader("apiKey") String apiKey, @PathVariable Long id) {
        pubSubService.removeSubscriber(id);
        return unauthorized(apiKey, HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<String> unauthorized(String apiKey, HttpStatus httpStatus) {
            if (!API_KEY.equals(apiKey)) {
                return new ResponseEntity<>("Invalid API key", HttpStatus.UNAUTHORIZED);
            }
        return new ResponseEntity<>(httpStatus);
    }
}