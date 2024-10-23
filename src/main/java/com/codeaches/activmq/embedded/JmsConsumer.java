package com.codeaches.activmq.embedded;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class JmsConsumer {
    @Autowired
    private OutboundService outboundService;

    Logger log = LoggerFactory.getLogger(JmsConsumer.class);

    @JmsListener(destination = "inBound")
    public void receiveInBound(String message) {
	log.info("Received message on inBound='{}'", message);
    }

    @JmsListener(destination = "outBound")
    public Type receiveOutBound(String message) {
        log.info("Received message on outBound='{}'", message);
        
        return outboundService.postOutBound(message);
    }    
}
