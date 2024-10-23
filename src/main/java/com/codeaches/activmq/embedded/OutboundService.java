package com.codeaches.activmq.embedded;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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
    public Type postOutBound(String message) {
        //HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);
        // HttpEntity<AddressDTO> entity = new HttpEntity<>(null, headers);
        log.info("Handling message='{}'", message);
        MyRestResponse restResponse = new MyRestResponse();

        try {
            String restResponseString =  restTemplate.postForObject(ADDRESS_SERVICE_URL, 
                null, String.class);

            restResponse.setResponseText(restResponseString);
            return restResponse;
        } catch (Exception e) {
            log.error(e.getMessage());
            restResponse.setResponseText(e.getMessage());
            return restResponse;            
        }
    }

    private Type fallbackMethod(Exception e) {
        return new Failure("Address service is not responding properly");
    }
}