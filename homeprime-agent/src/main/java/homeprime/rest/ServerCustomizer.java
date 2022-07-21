package homeprime.rest;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import homeprime.agent.config.pojos.Agent;
import homeprime.core.exception.ThingException;
import homeprime.core.model.readers.config.ConfigurationReader;
import homeprime.core.properties.ThingProperties;

/**
 * Customize Spring REST server settings.
 *
 * @author Milan Ramljak
 *
 */
@Component
public class ServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        try {
            final Agent agent = ConfigurationReader.getConfiguration().getAgent();
            // get agent port from agent configuration
            factory.setPort(agent.getPort());
            // if ssl settings exist, configure Spring to use them
            if (agent.getSsl() != null) {
                final Ssl sslConfigurator = new Ssl();
                sslConfigurator.setKeyAlias(agent.getSsl().getKeyAlias());
                sslConfigurator.setKeyStoreType(agent.getSsl().getKeyStoreType());
                sslConfigurator.setKeyStore(agent.getSsl().getKeyStore());
                sslConfigurator.setKeyStorePassword(agent.getSsl().getKeyStorePassword());
                factory.setSsl(sslConfigurator);
            }
        } catch (ThingException e) {
            factory.setPort(ThingProperties.getInstance().getDefaultRestPort());
        }
    }
}