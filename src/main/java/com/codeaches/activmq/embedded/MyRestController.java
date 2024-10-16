package com.codeaches.activmq.embedded;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class MyRestController {

    Logger log = LoggerFactory.getLogger(MyRestController.class);

    @Autowired
    JmsProducer jmsProducer;

    @PostMapping("/send")
    public void sendDataToJms(@RequestParam String message) {
        log.info("Received request on REST Controller with payload='{}'", message);
    	jmsProducer.send(message);
        log.info("Request put on queue - responding to REST client");
    }
}
