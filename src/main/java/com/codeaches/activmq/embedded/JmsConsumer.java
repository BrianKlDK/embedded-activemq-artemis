package com.codeaches.activmq.embedded;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class JmsConsumer {
    @Autowired
    private RestTemplate restTemplate = new RestTemplate();

    Logger log = LoggerFactory.getLogger(JmsConsumer.class);

    @JmsListener(destination = "inBound")
    public void receiveInBound(String message) {
	log.info("Received message on inBound='{}'", message);
    }

    @JmsListener(destination = "outBound")
    public void receiveOutBound(String message)
            throws Exception {
        URI uri;
        boolean error = false;

        log.info("Received message on outBound='{}'", message);
        

        try {
            uri = new URI("http://localhost:8080/dev/null");
            ResponseEntity<String> result = restTemplate.postForEntity(uri, message, String.class);
            log.info(result.toString());
        } catch (Exception e) {
            log.error(e.toString());
            error = true;
        }

        if (error) {
            throw new Exception("Exception");
        }
    }
    
}
