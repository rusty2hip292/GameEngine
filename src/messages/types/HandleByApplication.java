package messages.types;

public abstract class HandleByApplication extends messages.Message {

	public Class<? extends messages.Message> clazz() {
		return HandleByApplication.class;
	}
}
