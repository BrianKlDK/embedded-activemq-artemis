package com.codeaches.activmq.embedded;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;
// https://activemq.apache.org/components/artemis/documentation/javadocs/javadoc-latest/org/apache/activemq/artemis/core/config/Configuration.html
@Configuration
public class ArtemisConfig implements ArtemisConfigurationCustomizer {
    Logger log = LoggerFactory.getLogger(ArtemisConfig.class);

    @Override
    public void customize(org.apache.activemq.artemis.core.config.Configuration configuration) {
        //configuration.addAcceptorConfiguration("remote", "tcp://0.0.0.0:61616");
        log.info("Artemis config: {}", configuration.isJDBC());
    }
}