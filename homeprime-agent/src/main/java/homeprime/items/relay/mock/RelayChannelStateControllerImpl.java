package homeprime.items.relay.mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import homeprime.core.exceptions.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.items.relay.RelayChannelStateController;
import homeprime.items.relay.config.enums.RelayType;
import homeprime.items.relay.config.pojos.RelayBoards;
import homeprime.items.relay.config.pojos.RelayChannel;
import homeprime.items.relay.config.reader.RelayConfigReader;

/**
 * Manage relay channel pin state connected as mocked system.
 * 
 * @author Milan Ramljak
 */
public class RelayChannelStateControllerImpl implements RelayChannelStateController {

    private static Map<Integer, Boolean> portStates = new HashMap<Integer, Boolean>();

    @Override
    public Boolean readChannelState(RelayChannel relayChannelData) throws ThingException {
        if (relayChannelData == null) {
            throw new ThingException(
                    "ERROR RelayChannelStateControllerImpl.readChannelState() Cannot check relay channel state if RelayChannel pojo is null");
        }
        final int relayChannelPin = relayChannelData.getPin();
        Boolean state = portStates.get(relayChannelPin);

        final RelayType relayType = relayChannelData.getRelayType();
        if (relayType == RelayType.NC) {
            IoTLogger.getInstance().info("Reversing relay channel state for NC type of channel.");
            state = !state;
        }
        IoTLogger.getInstance().info("Current state of relay channel: " + relayChannelData.getName() + " is " + state);
        return state;
    }

    @Override
    public void toggleChannelState(RelayChannel relayChannelData) throws ThingException {
        final int relayChannelPin = relayChannelData.getPin();
        Boolean currentState = portStates.get(relayChannelPin);
        portStates.put(relayChannelPin, !currentState);
        IoTLogger.getInstance().info("Toggling current state of relay channel: " + relayChannelData.getName());
    }

    @Override
    public void setChannelState(RelayChannel relayChannelData, boolean newState) throws ThingException {
        final int relayChannelPin = relayChannelData.getPin();
        portStates.put(relayChannelPin, newState);
        IoTLogger.getInstance().info("State of relay channel: " + relayChannelData.getName() + " set to " + newState);
    }

    @Override
    public void initialize() throws ThingException {
        final RelayBoards relayBoards = RelayConfigReader.getRelayBoards();
        if (relayBoards != null) {
            List<RelayChannel> relayChannels = relayBoards.getRelayBoards().get(0).getRelayChannels();
            if (!relayChannels.isEmpty()) {
                for (RelayChannel relayChannel : relayChannels) {
                    if (relayChannel.getRelayType() == RelayType.NO) {
                        setChannelState(relayChannel, true);
                    } else {
                        setChannelState(relayChannel, false);
                    }
                }
            }
        }

    }

}
