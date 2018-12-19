package messages;

import messages.types.HandleByApplication;

public class Exit extends HandleByApplication {

	protected static String type = "exit";
	
	public String toString() {
		return type;
	}
}
