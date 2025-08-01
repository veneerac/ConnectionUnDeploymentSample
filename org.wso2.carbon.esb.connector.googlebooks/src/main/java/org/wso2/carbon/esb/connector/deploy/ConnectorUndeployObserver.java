package org.wso2.carbon.esb.connector.deploy;

import org.apache.synapse.config.AbstractSynapseObserver;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.libraries.model.Library;
import org.wso2.carbon.connector.core.connection.ConnectionHandler;
import org.wso2.carbon.esb.connector.utils.Const;

/**
 * Singleton class to manage connector-related connections.
 *
 * Developers using this class (especially in the context of WSO2 File Connector or similar)
 * should handle the connection lifecycle according to their specific use case.
 *
 * - Connection initialization should be done during the init() phase (via ManagedLifecycle).
 * - Cleanup logic (such as closing streams, sessions, etc.) should be explicitly handled
 *   by the developer when required.
 *
 * ⚠ Note: The shutdownConnections() method is intentionally left blank.
 * Developers are expected to implement appropriate shutdown logic here
 * depending on how the connection is used in their connector implementation.
 *
 * For reference or further enhancement, see the WSO2 File Connector implementation.
 */


public class ConnectorUndeployObserver extends AbstractSynapseObserver {

    private SynapseConfiguration synapseConfiguration;

    public ConnectorUndeployObserver(SynapseConfiguration synapseConfiguration) {
        this.synapseConfiguration = synapseConfiguration;
    }

    /**
     * Handles cleanup when a connector library is un-deployed.
     * Shuts down connections and unregisters the observer from Synapse configuration.
     */

    @Override
    public void synapseLibraryRemoved(Library library) {
        String libraryPath = library.getFileName();
        // Extract the library name from the full path
        String libraryName = libraryPath.substring(libraryPath.lastIndexOf("/"));
        log.info("---- Connector is being un-deployed from the synapseLibrary ----");

        // Check if the un-deployed library is a connector
        if (libraryName.contains(Const.CONNECTOR_LIBRARY_NAME)
                && library.getPackage().equals(Const.CONNECTOR_LIBRARY_PACKAGE_TYPE)) {
                log.info("---- Connector is being un-deployed. Cleaning up connections. ----");
            // Shut down connector connections
            ConnectionHandler.getConnectionHandler().shutdownConnections(Const.CONNECTOR_NAME);
            // Unregister this observer from the Synapse configuration during un-deployment
            synapseConfiguration.unregisterObserver(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof ConnectorUndeployObserver);
    }

    @Override
    public int hashCode() {
        return ConnectorUndeployObserver.class.hashCode();
    }
}
