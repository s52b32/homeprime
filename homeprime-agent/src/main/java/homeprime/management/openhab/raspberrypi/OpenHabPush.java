package homeprime.management.openhab.raspberrypi;

import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;

import homeprime.agent.config.pojos.Management;
import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.readers.items.contact.ContactsConfigReader;
import homeprime.core.model.readers.items.relay.RelayConfigReader;
import homeprime.core.model.readers.items.temperature.TemperatureConfigReader;
import homeprime.items.contact.config.enums.ContactType;
import homeprime.items.contact.config.pojos.ContactSensor;
import homeprime.items.relay.config.pojos.Relay;
import homeprime.items.temperature.TemperatureSensorControllerFactory;
import homeprime.items.temperature.config.pojos.TemperatureSensor;
import homeprime.management.openhab.client.OpenHabRestApiClient;

/**
 *
 * Runnable for management mode of type push.
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
            // create gpio controller
            final GpioController gpio = GpioFactory.getInstance();
            // get all contact sensors known to agent
            final List<ContactSensor> contactSensors = ContactsConfigReader.getContacts();
            if (!contactSensors.isEmpty()) {
                for (ContactSensor contactSensor : contactSensors) {
                    final String appId = contactSensor
                            .getCustomPropertyByName(management.getAppType().toString().toLowerCase() + "Id");
                    if (OpenHabRestApiClient.checkItemExists(management, appId)) {
                        final GpioPinDigitalInput contact = getOrProvisionGpio(gpio, contactSensor);
                        contact.setDebounce(contactSensor.getDebounceTime());
                        if (contactSensor.getContactType() != ContactType.PushSwitch) {
                            // create and register gpio pin listener
                            contact.addListener(new GpioPinListenerDigital() {

                                @Override
                                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                                    OpenHabRestApiClient.updateItemState(management, appId,
                                            translateContactState(contactSensor, event));
                                }

                            });
                            IoTLogger.getInstance()
                                    .info("Item with id (" + appId + ") exists in OpenHAB. Event listener started");
                        } else {
                            // create and register gpio pin listener
                            contact.addListener(new GpioPinListenerDigital() {
                                Long lastEventTime = System.currentTimeMillis();

                                @Override
                                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                                    Long currentEventTime = System.currentTimeMillis();
                                    // consider only triggers
                                    if (event.getState().isHigh()) {
                                        if (currentEventTime - lastEventTime > contactSensor.getPushDurationTime()) {
                                            // indicate long hold of push switch
                                            OpenHabRestApiClient.updateItemState(management, appId, "LONG");
                                        } else {
                                            OpenHabRestApiClient.updateItemState(management, appId, "SHORT");
                                        }
                                    } else {
                                        lastEventTime = currentEventTime;
                                    }
                                }

                            });
                            IoTLogger.getInstance().info("Item with id (" + appId
                                    + ") exists in OpenHAB. Event listener started for push switch");
                        }
                    } else {
                        IoTLogger.getInstance().error("No item with id (" + appId + ") in OpenHAB");
                    }
                }
            }

            // TODO: investigate relay push events
            // // get all relays known to agent
            // final List<Relay> relays = RelayConfigReader.getRelayList();
            // if (!relays.isEmpty()) {
            // for (Relay relay : relays) {
            // final String appId = relay
            // .getCustomPropertyByName(management.getAppType().toString().toLowerCase() + "Id");
            // if (OpenHabRestApiClient.checkItemExists(management, appId)) {
            // final GpioPinDigitalOutput output = RelayStateControllerImpl.getOrProvisionGpio(gpio, relay);
            // // create and register gpio pin listener
            // output.addListener(new GpioPinListenerDigital() {
            //
            // @Override
            // public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            // OpenHabRestApiClient.updateItemState(management, appId,
            // translateOutputState(relay, event));
            // }
            //
            // });
            // IoTLogger.getInstance()
            // .info("Item with id (" + appId + ") exists in OpenHAB. Event listener started");
            // } else {
            // IoTLogger.getInstance().error("No item with id (" + appId + ") in OpenHAB");
            // }
            // }
            // }

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
                            translateOutputState(relay, Gpio.digitalRead(relay.getPin()) == 0));
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
                            translateContactState(contactSensor, Gpio.digitalRead(contactSensor.getPin()) == 1));
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
                    try {
                        OpenHabRestApiClient.updateItemState(management, appId,
                                Float.toString(TemperatureSensorControllerFactory.getTemperatureSensorReader()
                                        .readTemperature(tempSensor)));
                    } catch (ThingException e) {
                        IoTLogger.getInstance().error("Failed to read temperature sensor value.");
                    }
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
        // remove all GPIO listeners
        GpioFactory.getInstance().removeAllListeners();
        this.isRunning = false;
        IoTLogger.getInstance().info("Post RUN");
    }

    /**
     * Helper method to check and return already provision GPIO pin or provision it and then return.
     *
     * @param gpio
     * @param contactSensor
     * @return {@code GpioPinDigitalInput} object of provisioned GPIO pin
     */
    private GpioPinDigitalInput getOrProvisionGpio(GpioController gpio, ContactSensor contactSensor) {
        if (gpio.getProvisionedPin(RaspiPin.getPinByAddress(contactSensor.getPin())) != null) {
            return (GpioPinDigitalInput) gpio.getProvisionedPin(RaspiPin.getPinByAddress(contactSensor.getPin()));
        } else {
            return gpio.provisionDigitalInputPin(RaspiPin.getPinByAddress(contactSensor.getPin()),
                    getContactPullResistance(contactSensor));
        }
    }

    private static PinPullResistance getContactPullResistance(ContactSensor contact) {
        switch (contact.getWiringType()) {
            case PullDown:
                return PinPullResistance.PULL_DOWN;
            case PullUp:
                return PinPullResistance.PULL_UP;
            default:
                return PinPullResistance.OFF;
        }
    }

    /**
     * Helper method to translate contact state from Raspberry Pi value to OpenHAB compliant value.
     *
     * @param contactSensor
     * @param contactEvent  - event object from GPIO listener.
     * @return string representing OpenHAB contact state.
     */
    private static String translateContactState(ContactSensor contactSensor,
            GpioPinDigitalStateChangeEvent contactEvent) {
        switch (contactSensor.getWiringType()) {
            case PullDown:
                if (contactEvent.getState().isHigh()) {
                    return "CLOSED";
                }
                return "OPEN";
            case PullUp:
                if (contactEvent.getState().isHigh()) {
                    return "OPEN";
                }
                return "CLOSED";
            default:
                if (contactEvent.getState().isHigh()) {
                    return "CLOSED";
                }
                return "OPEN";
        }
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
     * Helper method to translate relay state from Raspberry Pi value to OpenHAB compliant value.
     *
     * @param relay
     * @param event - event object from GPIO listener.
     * @return string representing OpenHAB relay state.
     */
    private static String translateOutputState(Relay relay, GpioPinDigitalStateChangeEvent event) {
        switch (relay.getRelayType()) {
            case NC:
                if (event.getState().isHigh()) {
                    return "OFF";
                }
                return "ON";
            case NO:
                if (event.getState().isHigh()) {
                    return "ON";
                }
                return "OFF";
            default:
                if (event.getState().isHigh()) {
                    return "ON";
                }
                return "OFF";
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
