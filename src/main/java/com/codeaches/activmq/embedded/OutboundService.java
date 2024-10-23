package com.codeaches.activmq.embedded;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class OutboundService {
    @Autowired
    private RestTemplate restTemplate;
    private static final String SERVICE_NAME = "jmsconsumer";
    private static final String ADDRESS_SERVICE_URL = "http://localhost:8000/dev/null";
    
    Logger log = LoggerFactory.getLogger(OutboundService.class);

    @CircuitBreaker(name = SERVICE_NAME, fallbackMethod = "fallbackMethod")
    public Type postOutBound(String message) throws ResourceAccessException {
        log.info("Handling message='{}'", message);
        MyRestResponse restResponse = new MyRestResponse();

        String restResponseString =  restTemplate.postForObject(ADDRESS_SERVICE_URL, 
              null, String.class);

        log.info("Response from restTemplate: {}", restResponseString);
        
        restResponse.setResponseText(restResponseString);
        return restResponse;
    }

    private Type fallbackMethod(String message, Exception e) {
        //return new Failure("Address service is not responding properly");
        return new Failure("External dev null API not working. Exception info='");
    }
}