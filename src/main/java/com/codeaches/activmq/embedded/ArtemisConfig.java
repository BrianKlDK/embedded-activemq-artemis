package com.codeaches.activmq.embedded;

import org.apache.activemq.artemis.core.config.storage.DatabaseStorageConfiguration;
import org.apache.activemq.artemis.core.config.StoreConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
// https://activemq.apache.org/components/artemis/documentation/javadocs/javadoc-latest/org/apache/activemq/artemis/core/config/Configuration.html

@ConfigurationProperties(prefix = "artemis.config")
@Configuration
public class ArtemisConfig implements ArtemisConfigurationCustomizer {
    Logger log = LoggerFactory.getLogger(ArtemisConfig.class);

    @Value("${artemis.config.jdbc-connection-url}")
    private String jdbcConnectionURL;

    @Value("${artemis.config.jdbc-driver-class}")
    private String jdbcDriverClass;

    @Value("${artemis.config.jdbc-username}")
    private String jdbcUsername;

    @Value("${artemis.config.jdbc-password}")
    private String jdbcPassword;

    @Override
    public void customize(org.apache.activemq.artemis.core.config.Configuration configuration) {
        //configuration.addAcceptorConfiguration("remote", "tcp://0.0.0.0:61616");
        log.info("Artemis config: isJDBC: {}", configuration.isJDBC());
        log.info("Artemis config: isPersistenceEnabled: {}", configuration.isPersistenceEnabled());
        log.info("Artemis config: toString {}", configuration.toString());

        log.info("Configuring Artemis JDBC persistence");
        DatabaseStorageConfiguration storeConfiguration = new DatabaseStorageConfiguration();
        log.info("Configuring Artemis JDBC persistence - connection URL='{}'", jdbcConnectionURL);
        storeConfiguration.setJdbcConnectionUrl(jdbcConnectionURL);
        storeConfiguration.setJdbcDriverClassName(jdbcDriverClass);
        storeConfiguration.setJdbcUser(jdbcUsername);
        storeConfiguration.setJdbcPassword(jdbcPassword);
        configuration.setStoreConfiguration(storeConfiguration);
        //log.info("Artemis config: toString {}", configuration.toString());
    }
}