# Current implementation

````Circuit Breaker```` is implemented on ````OutboundService.postOutBound```` method currently.

Two ways of getting to ````OutboundService```` class:

## Straight chained REST-to-REST request

This flow goes as follows:

- Through ````MyRestController.devNullReceiveString```` the Spring Boot application exposes an endpoint called ````/dev/null```` for POST'ing a ````RequestBody````:

    ````HTTP
    POST http://localhost:8080/dev/null HTTP/1.1
    Content-Type: application/json

    {
        "payload": "this is the payload"
    }
    ````

- ````MyRestController.devNullReceiveString```` invokes ````OutboundService.postOutBound()```` passing the ````RequestBody```` as input parameter.
- ````OutboundService.postOutBound```` uses ````RestTemplate```` to make a request to ````http://localhost:8000/dev/null````. **Note**: the difference in port, though the URI is almost identical! & URI is hardcoded in method/class.
- ````OutboundService.postOutBound```` is annotated with ````@CircuitBreaker(name = SERVICE_NAME, fallbackMethod = "fallbackMethod")````, which means that if the request from ````RestTemplate```` throws any exception, the ````fallbackMethod```` is invoked. **Note**: the specified fallbackMethod must match the annotated method in return type and input parameters - several can be created to match specific exceptions.
- The annotation with ````@CircuitBreaker```` means that any result of the method (success or exception) is a signal to the specified Circuit Breaker to react to with regards to change of state, etc. as configured in ````application.properties````.
- The result of ````OutboundService.postOutBound````/````OutboundService.fallbackMethod```` (depending on the CircuitBreaker) is propagated back to ````MyRestController.devNullReceiveString```` returning it to the client in the ````ResponseBody````.

Invoking the endpoint will result in a state change that can be monitored by ````GET http://localhost:8080/actuator/health HTTP/1.1````. Counts of requests (failed/buffered/succesfull) are updated for every requests, state change depends on thresholds, timeouts etc. configuration of the given circuit breaker, as stored in ````application.properties```` .

## REST-to-MQ-to-REST for buffering, retries and resilience

This flow works as follows:

- Through ````MyRestController.requestReceiver```` the Spring Boot application exposes an endpoint called
 ````/response```` for POST'ing a ````RequestBody````:

    ````HTTP
    POST http://localhost:8080/response HTTP/1.1
    Content-Type: application/json

    {
        "id": 1,
        "payload": "this is the response"
    }
    ````

- ````MyRestController.requestReceiver```` looks for the ````Entity```` ````PortfolioRequest```` matching the ````id```` property of the ````RequestBody```` from the ````PortfolioRequestRepository````.
- If a matching ````PortfolioRequest```` is found, the ````RequestBody```` is put on an internal/embedded MQ called ````outBound```` using ````JmsProducer.send````, the status of the ````PortfolioRequest```` is updated, and an ````ACCEPTED```` status is returned to the REST client. The client is essentially promised that the application will take it from here - a "fire and forget" style pattern.
- Asynchrously the ````JmsConsumer.receiveOutBound```` listens to the internal/embedded MQ called ````outBound````, where the message is consumed.
- ````JmsConsumer.receiveOutBound```` invokes ````OutboundService.postOutBound```` which is the same as above, and thus the flows is the same from here.

### Notes

Current issues:

- Without an exception in the ````JmsConsumer.receiveOutBound````/````@JmsListener```` annotated method, the message is simply taken off the queue and the consumption is commited - but with a failure on the destination, the message ends up being lost.

  The circuit breaker still trips/updates/changes according to configuration with whatever result from/exception is thrown in ````OutboundService.postOutBound````.

- Throwing an exception in ````JmsConsumer.receiveOutBound````/````@JmsListener```` means that the consumption is not committed, and the message remains on the queue. It is retried automatically by ````JmsConsumer.receiveOutBound````/````@JmsListener````. Standard config seems to be 10 retries immediately after each other.

  On the 10th failue, the message is moved to DLQ.

Thoughts:

- Can listener be configured to delay retries a little, so ````@Retry```` is not needed on ````JmsConsumer.receiveOutBound````/````@JmsListener```` for dealing with transient errors?
- How to deal with DLQ?
- Should there be some startup-function that looks at DLQ and retries? 
- Should there be some schedule to attempt DLQ reprocessing?
- Should DLQ reprocessing be triggered by circuit breaker state change?
- Should DLQ reprocessing only be attempted if circuit breaker is NOT open?

## Various notes

Command to run local MySQL in docker for Artemis MQ JDBC persistance, and JPA/Repositories of the app.:

- docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mysql -v /my/own/datadir:/var/lib/mysql mysql:8.2.0
