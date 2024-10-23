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
        //HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);
        // HttpEntity<AddressDTO> entity = new HttpEntity<>(null, headers);
        log.info("Handling message='{}'", message);
        MyRestResponse restResponse = new MyRestResponse();

        

        //try {
            //throw new ResourceAccessException("Fuck off!");
            String restResponseString =  restTemplate.postForObject(ADDRESS_SERVICE_URL, 
              null, String.class);

            log.info("Response from restTemplate: {}", restResponseString);
        
            restResponse.setResponseText(restResponseString);
            restResponse.setResponseText("HM");
            return restResponse;
        /*} catch (Exception e) {
            log.error(e.getClass().toString());
            log.error(e.getMessage());
            restResponse.setResponseText("From exception:" + e.getMessage());
            throw new ResourceAccessException("From exception:" + e.getMessage());
            //return restResponse;
            
        }/**/
    }

    /*private Type fallbackMethod(java.lang.String message, java.lang.Throwable e) {
        //return new Failure("Address service is not responding properly");
        return new Failure("v3 External dev null API not working. Exception info='");
    }*/


    private Type fallbackMethod(String message, Exception e) {
        //return new Failure("Address service is not responding properly");
        return new Failure("External dev null API not working. Exception info='");
    }

/*    private Type fallbackMethod(ResourceAccessException e) {
        //return new Failure("Address service is not responding properly");
        return new Failure("v2 External dev null API not working. Exception info='");
    }*/

 
   /* private Type fallbackMethod(Exception e) {
        //return new Failure("Address service is not responding properly");
        return new Failure("v4 External dev null API not working. Exception info='");
    } */
}