package systems;

public class Systems {

	public static void startAll(game.Application wrapper) {
		MessageBus.bus(wrapper).start();
		new Console(wrapper).start();
		new Camera(wrapper).start();
		MessageBus.bus(null).addPriorityMessage(new messages.Start());
	}
	
	public static void stopAll() {
		MessageBus.bus(null).stopAllRegisteredSystems();
	}
}
