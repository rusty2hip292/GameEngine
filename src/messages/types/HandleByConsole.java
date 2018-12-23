package messages.types;

public abstract class HandleByConsole extends messages.Message {

	public Class<? extends messages.Message> clazz() {
		return HandleByConsole.class;
	}
}
