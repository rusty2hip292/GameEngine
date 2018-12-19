package messages;

import messages.types.HandleByApplication;

public class Start extends HandleByApplication {

	protected static String type = "start";
	
	public String toString() {
		return type;
	}
}
