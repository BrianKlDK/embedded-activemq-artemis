package com.codeaches.activmq.embedded;

import org.apache.activemq.artemis.core.config.StoreConfiguration;
import org.apache.activemq.artemis.core.config.storage.DatabaseStorageConfiguration;
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
        log.info("Artemis config: isJDBC: {}", configuration.isJDBC());
        log.info("Artemis config: isPersistenceEnabled: {}", configuration.isPersistenceEnabled());
        log.info("Artemis config: toString {}", configuration.toString());
        DatabaseStorageConfiguration storeConfiguration = new DatabaseStorageConfiguration();
        storeConfiguration.setJdbcConnectionUrl("jdbc:mysql://172.17.0.2:3306/artemis");
        storeConfiguration.setJdbcDriverClassName("com.mysql.cj.jdbc.Driver");
        storeConfiguration.setJdbcUser("artemis");
        storeConfiguration.setJdbcPassword("artemis");
        configuration.setStoreConfiguration(storeConfiguration);
        //log.info("Artemis config: toString {}", configuration.toString());
    }
}