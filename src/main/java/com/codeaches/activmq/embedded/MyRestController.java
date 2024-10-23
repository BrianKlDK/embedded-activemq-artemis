package com.codeaches.activmq.embedded;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class MyRestController {
    Logger log = LoggerFactory.getLogger(MyRestController.class);

    @Autowired
    JmsProducer jmsProducer;

    @Autowired
    private PortfolioRequestRepository portfolioRequestRepository;

    @Autowired
    private OutboundService outboundService;

    private ObjectMapper mapper = new ObjectMapper();


    @PostMapping("/response")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public @ResponseBody String responseReceiver(@RequestBody String requestBody)
            throws JsonMappingException, JsonProcessingException {
        
        PortfolioRequest pRequest = new PortfolioRequest();
        int refId;

        log.info("Blank pRequest='{}'", pRequest.getId());

        log.info("Received reponse on REST Controller with payload='{}'", requestBody);

        JsonNode node = mapper.readValue(requestBody, JsonNode.class);

        if (node.has("id")) {
            refId = Integer.parseInt(node.at("/id").asText());
            pRequest = portfolioRequestRepository.findById(refId).orElse(new PortfolioRequest());
        } else {
            log.warn("Response payload invald");
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Error: response payload invalid");
        }

        if (pRequest.getId() == null) {
            log.warn("Response to unknown request id={}", refId);
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("Error: response to unknown request id={}", refId));
        } else {
            pRequest.setStatus("response-received");
            // We'd also do a model/entity and repository for responses as well - but skipped here for simplicity
            portfolioRequestRepository.save(pRequest);
    	    jmsProducer.send("outBound", requestBody);
            log.info("Request updated, and Response put on queue - acknowledging client");
            return "all good";
        }
    }

    @PostMapping("/request")
    public @ResponseBody String requestReceiver(@RequestBody String requestBody)
            throws JsonMappingException, JsonProcessingException {

        log.info("Received request on REST controller with payload = '{}'", requestBody);

        PortfolioRequest pRequest = new PortfolioRequest();

        JsonNode node = mapper.readValue(requestBody, JsonNode.class);

        if (node.has("id")) 
            pRequest.setId(Integer.parseInt(node.at("/id").asText()));
        
        if (node.has("payload")) 
            pRequest.setPayload(node.at("/payload").asText());

        pRequest.setStatus("requested");

        portfolioRequestRepository.save(pRequest);
        jmsProducer.send("inBound", requestBody);

        return ("Saved and sent");
    }

    @PostMapping("/dev/null")
    public @ResponseBody String devNullReceiveString(@RequestBody String requestBody) {

        log.info("Black hole recevied payload = '{}'", requestBody);
        return outboundService.postOutBound(requestBody).toString();
    }
}
