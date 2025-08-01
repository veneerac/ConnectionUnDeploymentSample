package org.wso2.carbon.esb.connector;

import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.ManagedLifecycle;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.esb.connector.deploy.ConnectorUndeployObserver;

public class googleBooksConnector extends AbstractConnector implements ManagedLifecycle {

    private static Log log = LogFactory.getLog(googleBooksConnector.class);

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {
        Object templateParam = getParameter(messageContext, "generated_param");
        log.info("---- googleBooks sample connector received message ----");
        try {
            log.info("---- googleBooks sample connector received ----: " + templateParam);
        } catch (Exception e) {
            throw new ConnectException(e);
        }
    }

    // ManagedLifecycle method — now will be invoked by MI
    @Override
    public void init(SynapseEnvironment synapseEnvironment) {
        log.info("---- Connection initialization is handled via the init() method ----");
        SynapseConfiguration synapseConfig = synapseEnvironment.getSynapseConfiguration();
        synapseConfig.registerObserver(new ConnectorUndeployObserver(synapseConfig));
    }

    @Override
    public void destroy() {
        log.info(" ---- Developers should not rely on destroy() for cleanup operations during undeployment ----");

    }
}
