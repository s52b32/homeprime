package homeprime.core.management.openhab;

import homeprime.agent.config.pojos.Thing;
import homeprime.management.IManagement;

public class Management implements IManagement {

	private Thing thingConfiguration = null;

	public Management(Thing thingConfiguration) {
		this.thingConfiguration = thingConfiguration;
	}

	@Override
	public Boolean getContactState(String itemName) {
		return null;
	}

	@Override
	public Boolean setContactState(String itemName, boolean state) {
		return RestApiUtils.setContactState(thingConfiguration.getManagement().getIp(),
				thingConfiguration.getManagement().getPort(), itemName, state);
	}

	@Override
	public Boolean getRelayState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setRelayState() {
		// TODO Auto-generated method stub
		return null;
	}

}
