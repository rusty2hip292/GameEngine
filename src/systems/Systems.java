package systems;

public class Systems {
	
	public static boolean running = true;

	public static void startAll(game.Application wrapper) {
		MessageBus.bus(wrapper).start();
		new Console(wrapper).start();
		new CameraSystem(wrapper).start();
		MessageBus.bus(null).addPriorityMessage(new messages.Start());
	}
	
	public static void stopAll() {
		MessageBus.bus(null).stopAllRegisteredSystems();
		running = false;
	}
}
