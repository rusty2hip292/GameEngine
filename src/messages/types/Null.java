package messages.types;

public abstract class Null extends messages.Message {

	public Class<? extends messages.Message> clazz() {
		return Null.class;
	}
}
