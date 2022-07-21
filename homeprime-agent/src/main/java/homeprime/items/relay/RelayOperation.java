package homeprime.items.relay;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RelayOperation {

	on("on"),

	off("off"),

	toggle("toggle");

	private String operation;

	RelayOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public String toString() {
		return operation;
	}

	@JsonCreator
	public static RelayOperation fromText(String text) {
		if (text != null) {
			for (RelayOperation r : RelayOperation.values()) {
				if (r.operation.equals(text)) {
					return r;
				}
			}
		}
		return null;
	}

	public static String listValues() {
		String supported = "";
		for (RelayOperation r : RelayOperation.values()) {
			supported = supported + r.toString() + ", ";
		}
		return supported.trim();
	}
	
}
