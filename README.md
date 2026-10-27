# Connection Undeployment Sample

This sample shows how a WSO2 Micro Integrator connector can **shut down its connections cleanly when the connector is undeployed**. It follows the same approach as the WSO2 File Connector: a `ConnectorUndeployObserver` that listens for the connector library being removed.

## Why

`destroy()` on a connector is not a reliable place to release resources during undeployment. Registering a Synapse observer lets the connector react when its library is removed, so pooled connections are closed and the observer is unregistered.

## What's inside

| Path | Description |
| --- | --- |
| `org.wso2.carbon.esb.connector.googlebooks/` | Sample connector (Maven). `googleBooksConnector.init()` registers the observer. |
| `.../deploy/ConnectorUndeployObserver.java` | Extends `AbstractSynapseObserver`. On `synapseLibraryRemoved`, it calls `ConnectionHandler.shutdownConnections(...)` and unregisters itself. |
| `.../utils/Const.java` | Constants used to match the connector library and connection names. |
| `GoogleTestApi/` | WSO2 MI for VS Code integration project with the packaged connector and a `googlebooks_listVolume` proxy service. |

## How it works

1. When MI initializes the connector (it implements `ManagedLifecycle`), `init(SynapseEnvironment)` registers a `ConnectorUndeployObserver` with the `SynapseConfiguration`.
2. When a connector library is removed, Synapse calls `synapseLibraryRemoved(Library)`.
3. If the library name contains `Const.CONNECTOR_LIBRARY_NAME` and its package is `Const.CONNECTOR_LIBRARY_PACKAGE_TYPE`, the observer shuts down the connections for `Const.CONNECTOR_NAME` and unregisters itself.

## Adapting it to your connector

`Const.java` currently uses the File Connector values (`CONNECTOR_LIBRARY_NAME = "file-connector"`, `CONNECTOR_NAME = "file"`). Change these to your connector's library name and connection name, otherwise the observer will not match your connector when it is undeployed.

## Build the connector

```bash
cd org.wso2.carbon.esb.connector.googlebooks
mvn clean install
```

The connector ZIP is created under `target/`. Java 8 or later and access to the WSO2 Maven repository are required.

## Try it

1. Copy the built connector ZIP to `GoogleTestApi/src/main/wso2mi/resources/connectors/` (a built copy is already included).
2. Open `GoogleTestApi` in VS Code with the WSO2 Integrator: MI extension, then build and run it.
3. Invoke the `googlebooks_listVolume` proxy with a JSON body such as `{"searchQuery": "integration"}`.
4. Undeploy the CApp or remove the connector, and check the MI logs for:
   `Connector is being un-deployed. Cleaning up connections.`

## License

Licensed under the [Apache License 2.0](LICENSE).
