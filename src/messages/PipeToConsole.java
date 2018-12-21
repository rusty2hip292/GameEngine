package messages;

import messages.types.HandleByConsole;

public class PipeToConsole extends HandleByConsole {

	private Object o;
	
	protected final static String type = "console";
	
	public PipeToConsole(Object o) {
		this.o = o;
	}
	
	public String toString() {
		return "console " + o.toString();
	}
	
	public Object getMsg() {
		return o;
	}

}
