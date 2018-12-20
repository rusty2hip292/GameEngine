package systems;

import game.Application;
import messages.*;

public class CameraSystem extends System {

	private gameobjects.Camera camera = new gameobjects.Camera();
	
	public CameraSystem(Application owner) {
		super(owner);
	}

	@Override
	public void run() {
		while(running) {
			this.handleMessages();
		}
	}

	@Override
	protected void handleMessage(messages.Message m) {
		if(m instanceof messages.CameraMessage) {
			messages.CameraMessage temp = (messages.CameraMessage) m;
			camera.moveTo(temp.x, temp.y, temp.z, temp.a, temp.b, temp.c);
		}
	}

	protected Class<? extends messages.Message> messageTypeToHandle() {
		return messages.types.HandleByCamera.class;
	}

}
