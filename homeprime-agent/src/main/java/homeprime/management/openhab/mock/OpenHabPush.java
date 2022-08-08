package homeprime.management.openhab.mock;

import java.util.List;
import java.util.Random;

import homeprime.agent.config.pojos.Management;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.readers.items.contact.ContactsConfigReader;
import homeprime.core.model.readers.items.relay.RelayConfigReader;
import homeprime.core.model.readers.items.temperature.TemperatureConfigReader;
import homeprime.items.contact.config.pojos.ContactSensor;
import homeprime.items.relay.config.pojos.Relay;
import homeprime.items.temperature.config.pojos.TemperatureSensor;
import homeprime.management.openhab.client.OpenHabRestApiClient;

/**
 *
 * Runnable for management mode of type push for MOCK.
 *
 * @author Milan Ramljak
 *
 */
public class OpenHabPush implements Runnable {

    private boolean doStop = false;
    private boolean isRunning = false;
    private Management management = null;

    /**
     * Default constructor.
     *
     * @param management
     */
    public OpenHabPush(Management management) {
        this.management = management;
    }

    /**
     * Order thread stop.
     */
    public synchronized void doStop() {
        this.doStop = true;
    }

    /**
     * Check if thread is still running.
     *
     * @return {@code true} if running otherwise {@code false}.
     */
    public synchronized boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public void run() {
        // first sync states to actual state
        sync(management);
        if (OpenHabRestApiClient.isAlive(management)) {
            // get all contact sensors known to agent
            final List<ContactSensor> contactSensors = ContactsConfigReader.getContacts();
            if (!contactSensors.isEmpty()) {
                for (ContactSensor contactSensor : contactSensors) {
                    final String appId = contactSensor
                            .getCustomPropertyByName(management.getAppType().toString().toLowerCase() + "Id");
                    if (OpenHabRestApiClient.checkItemExists(management, appId)) {
                        OpenHabRestApiClient.updateItemState(management, appId,
                                translateContactState(contactSensor, (new Random()).nextBoolean()));
                        IoTLogger.getInstance()
                                .info("Item with id (" + appId + ") exists in OpenHAB. Event listener started");
                    } else {
                        IoTLogger.getInstance().error("No item with id (" + appId + ") in OpenHAB");
                    }
                }
            }

            // get all relays known to agent
            final List<Relay> relays = RelayConfigReader.getRelayList();
            if (!relays.isEmpty()) {
                for (Relay relay : relays) {
                    final String appId = relay
                            .getCustomPropertyByName(management.getAppType().toString().toLowerCase() + "Id");
                    if (OpenHabRestApiClient.checkItemExists(management, appId)) {
                        OpenHabRestApiClient.updateItemState(management, appId,
                                translateOutputState(relay, (new Random()).nextBoolean()));
                        IoTLogger.getInstance()
                                .info("Item with id (" + appId + ") exists in OpenHAB. Event listener started");
                    } else {
                        IoTLogger.getInstance().error("No item with id (" + appId + ") in OpenHAB");
                    }
                }
            }

            this.isRunning = true;

            // keep program running until user aborts
            while (keepRunning()) {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    IoTLogger.getInstance().error("Sleep interrupted");
                }
            }
            // clear all provisioned pins and activated listeners
            postRun();

        } else {
            IoTLogger.getInstance().error("Cannot start OpenHabPush runnable if OpenHAB is not alive");
            doStop();
        }
    }

    public static Boolean sync(Management management) {
        // Sync relay states known to agent
        final List<Relay> relays = RelayConfigReader.getRelayList();
        if (!relays.isEmpty()) {
            for (Relay relay : relays) {
                final String appId = relay
                        .getCustomPropertyByName(management.getAppType().toString().toLowerCase() + "Id");
                if (OpenHabRestApiClient.checkItemExists(management, appId)) {
                    OpenHabRestApiClient.updateItemState(management, appId,
                            translateOutputState(relay, (new Random()).nextBoolean()));
                    IoTLogger.getInstance().info("Item with id (" + appId + ") exists in OpenHAB. Synced.");
                } else {
                    IoTLogger.getInstance().error("No item with id (" + appId + ") in OpenHAB");
                }
            }
        }
        // Sync contact states known to agent
        final List<ContactSensor> contactSensors = ContactsConfigReader.getContacts();
        if (!contactSensors.isEmpty()) {
            for (ContactSensor contactSensor : contactSensors) {
                final String appId = contactSensor
                        .getCustomPropertyByName(management.getAppType().toString().toLowerCase() + "Id");
                if (OpenHabRestApiClient.checkItemExists(management, appId)) {
                    OpenHabRestApiClient.updateItemState(management, appId,
                            translateContactState(contactSensor, (new Random()).nextBoolean()));
                    IoTLogger.getInstance().info("Item with id (" + appId + ") exists in OpenHAB. Synced.");
                } else {
                    IoTLogger.getInstance().error("No item with id (" + appId + ") in OpenHAB");
                }
            }
        }
        // Sync temperature sensors known to agent
        final List<TemperatureSensor> tempSensors = TemperatureConfigReader.getTemperatureSesorList();
        if (!tempSensors.isEmpty()) {
            for (TemperatureSensor tempSensor : tempSensors) {
                final String appId = tempSensor
                        .getCustomPropertyByName(management.getAppType().toString().toLowerCase() + "Id");
                if (OpenHabRestApiClient.checkItemExists(management, appId)) {
                    OpenHabRestApiClient.updateItemState(management, appId, Float.toString((new Random()).nextFloat()));
                    IoTLogger.getInstance().info("Item with id (" + appId + ") exists in OpenHAB. Synced.");
                } else {
                    IoTLogger.getInstance().error("No item with id (" + appId + ") in OpenHAB");
                }
            }
        }
        return true;
    }

    /**
     * Check weather thread should still run.
     *
     * @return {@code true} to keep thread running otherwise {@code false}.
     */
    private synchronized boolean keepRunning() {
        return this.doStop == false;
    }

    /**
     * Helper method to clean out listeners once thread is finalized.
     */
    private void postRun() {
        this.isRunning = false;
        IoTLogger.getInstance().info("Post RUN");
    }

    /**
     * Helper method to translate contact state from Raspeberry PI to OpenHAB.
     *
     * @param contactSensor
     * @param contact
     * @return
     */
    private static String translateContactState(ContactSensor contactSensor, boolean contact) {
        switch (contactSensor.getWiringType()) {
            case PullDown:
                if (contact) {
                    return "CLOSED";
                }
                return "OPEN";
            case PullUp:
                if (contact) {
                    return "OPEN";
                }
                return "CLOSED";
            default:
                if (contact) {
                    return "CLOSED";
                }
                return "OPEN";
        }
    }

    /**
     * Helper method to translate relay connected to Raspberry PI GPIO pin to value compliant to OpenHAB.
     *
     * @param relay
     * @param output
     * @return
     */
    private static String translateOutputState(Relay relay, boolean output) {
        switch (relay.getRelayType()) {
            case NC:
                if (output) {
                    return "OFF";
                }
                return "ON";
            case NO:
                if (output) {
                    return "ON";
                }
                return "OFF";
            default:
                if (output) {
                    return "ON";
                }
                return "OFF";
        }
    }

}
